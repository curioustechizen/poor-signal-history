
package in.kmb.poorsig;

import in.kmb.poorsig.SignalStrengthProvider.TableSigStrength;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * The background Service which listens for signal strength changes and saves the history.
 * 
 * @author Kiran Rao
 */
public class SignalStrengthService extends Service {

	private TelephonyManager mTelephonyMgr;
	private ContentResolver mContentResolver;
	
	/**
	 * Previous Signal Strength.
	 */
	private SignalStrengthEnum previousSignalStrength;
	
	/**
	 * The current signal strength.
	 */
	private SignalStrength mSignalStrength;
	private ServiceState mServiceState;
	/**
	 * The previous timestamp.
	 */
	private long previousTimeStamp = System.currentTimeMillis();
	private PhoneStateListener mPhoneStateListener;

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		if (mTelephonyMgr == null) {
			mTelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		}
		if (mContentResolver == null) {
			mContentResolver = getContentResolver();
		}
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		startListeningForSignalStrength();
		return super.onStartCommand(intent, flags, startId);
	}
	

	/**
	 * Start listening for signal strength.
	 */
	private void startListeningForSignalStrength() {
		Log.d(Constants.TAG, "Started listening for signal strength changes");

		mPhoneStateListener = new PhoneStateListener() {

			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				Log.d(Constants.TAG, "Signal Strength Changed! New strength = "+signalStrength.getGsmSignalStrength());
				mSignalStrength = signalStrength;
				processSignalStrength();
				super.onSignalStrengthsChanged(signalStrength);
			}

			@Override
			public void onServiceStateChanged(ServiceState serviceState) {
				Log.d(Constants.TAG, "Service State Changed: "+serviceState.getState());
				mServiceState = serviceState;
				super.onServiceStateChanged(serviceState);
			}
		};

		mTelephonyMgr.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
						| PhoneStateListener.LISTEN_SERVICE_STATE);

	}

	/**
	 * Process signal strength.
	 */
	protected void processSignalStrength() {
		SignalStrengthEnum strengthEnumValue = Utils
				.getEnumForLevel(categorizeSignalStrength());

		/*
		 * If the new strength falls into a different bucket than the previous one,
		 * then add a record to the database.
		 */
		if (!strengthEnumValue.equals(previousSignalStrength)) {
			long currentTimeStamp =System.currentTimeMillis();
			String insertString = SignalStrengthEnum.UNKNOWN.toString();
			if(previousSignalStrength != null){
				insertString = previousSignalStrength.toString(); 
			}
			insertIntoDb(currentTimeStamp, previousTimeStamp,insertString);
			previousSignalStrength = strengthEnumValue;
			previousTimeStamp = currentTimeStamp;
		}
	}

	/**
	 * Insert into db.
	 * 
	 * @param currentTimeStamp
	 *            the current time stamp
	 * @param previousTimeStamp
	 *            the previous time stamp
	 * @param strengthLevel
	 *            the strength level
	 */
	private void insertIntoDb(long currentTimeStamp, long previousTimeStamp,
			String strengthLevel) {
		ContentValues cv = new ContentValues();
		cv.put(TableSigStrength.COLUMN_TIMESTAMP, currentTimeStamp);
		cv.put(TableSigStrength.COLUMN_PREVIOUS_TIMESTAMP, previousTimeStamp);
		cv.put(TableSigStrength.COLUMN_PREVIOUS_SIGNAL_STRENGTH_LEVEL,
				strengthLevel);
		mContentResolver.insert(TableSigStrength.CONTENT_URI, cv);
	}

	/**
	 * Checks if this is a CDMA device.
	 * 
	 * @return {@code true}, if is CDMA
	 */
	private boolean isCdma() {
		return (mSignalStrength != null) && !mSignalStrength.isGsm();
	}

	/**
	 * Checks whether the phone is in service.
	 * 
	 * @return {@code true}, if the phone is in service.
	 */
	private boolean hasService() {
		if (mServiceState != null) {
			switch (mServiceState.getState()) {
				case ServiceState.STATE_OUT_OF_SERVICE:
				case ServiceState.STATE_POWER_OFF:
					return false;
				default:
					return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Categorize signal strength.
	 * 
	 * @return the signal level (in number of bars) for the current GSM strength.
	 */
	private final int categorizeSignalStrength() {
		int signalLevel = -1;

		if (hasService()) {
			if (!isCdma()) {
				int asu = mSignalStrength.getGsmSignalStrength();

				// ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
				// asu = 0 (-113dB or less) is very weak
				// signal, its better to show 0 bars to the user in such cases.
				// asu = 99 is a special case, where the signal strength is
				// unknown.
				if (asu <= 2 || asu == 99)
					signalLevel = 0;
				else if (asu >= 12)
					signalLevel = 4;
				else if (asu >= 8)
					signalLevel = 3;
				else if (asu >= 5)
					signalLevel = 2;
				else
					signalLevel = 1;
			} else {
				signalLevel = getCdmaLevel();
			}
		}

		return signalLevel;
	}

	/**
	 * Gets the CDMA level.
	 * 
	 * @return the CDMA level
	 */
	private int getCdmaLevel() {
		final int cdmaDbm = mSignalStrength.getCdmaDbm();
		final int cdmaEcio = mSignalStrength.getCdmaEcio();
		int levelDbm = 0;
		int levelEcio = 0;

		if (cdmaDbm >= -75)
			levelDbm = 4;
		else if (cdmaDbm >= -85)
			levelDbm = 3;
		else if (cdmaDbm >= -95)
			levelDbm = 2;
		else if (cdmaDbm >= -100)
			levelDbm = 1;
		else
			levelDbm = 0;

		// Ec/Io are in dB*10
		if (cdmaEcio >= -90)
			levelEcio = 4;
		else if (cdmaEcio >= -110)
			levelEcio = 3;
		else if (cdmaEcio >= -130)
			levelEcio = 2;
		else if (cdmaEcio >= -150)
			levelEcio = 1;
		else
			levelEcio = 0;

		return (levelDbm < levelEcio) ? levelDbm : levelEcio;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.d(Constants.TAG, "Service being destroyed");
		mTelephonyMgr.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		//We don't require binding, so return null.
		return null;
	}
}
