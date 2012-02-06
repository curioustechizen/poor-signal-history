
package in.kmb.poorsig;

import in.kmb.poorsig.SignalStrengthProvider.TableSigStrength;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * The main Activity for displaying the Poor Signal History.
 * 
 * @author Kiran Rao
 */
public class PoorSigActivity extends Activity {

	/**
	 * List View to be used to for displaying the history.
	 */
	private ListView mListView;
	
	/**
	 * Custom {@code CursorAdapter} instance.
	 */
	private SignalStrengthCursorAdapter mCustomCursorAdapter;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
         * If this activity is being run for the very first time, 
         * then start our background service immediately.
         * 
         * This allows the service to be started soon after the app is installed.
         */
        
        
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if(!prefs.contains(Constants.KEY_FIRST_RUN)){
        	Log.d(Constants.TAG, "First Run - starting SignalStrengthService");
        	Intent startServiceIntent = new Intent(this, SignalStrengthService.class);
			startService(startServiceIntent);
			prefs.edit().putBoolean(Constants.KEY_FIRST_RUN, false).commit();
        }
        
        setContentView(R.layout.main);
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		displayWeakSignalHistory();
		super.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Refresh");
		return super.onPrepareOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		displayWeakSignalHistory();
		mCustomCursorAdapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Display weak signal history.
	 */
	private void displayWeakSignalHistory() {
		
		// First, get the cursor to the poor signal history.
		Cursor signalPeriodsCursor = listSignalPeriods(SignalStrengthEnum.VERY_POOR);
		Log.d(Constants.TAG, "In displayWeakSignalHistory(), cursor size = "+signalPeriodsCursor.getCount());
		

		//Then, set it into a CursorAdapter.
		mCustomCursorAdapter = new SignalStrengthCursorAdapter(this, signalPeriodsCursor);
		
		mListView = (ListView) findViewById(R.id.lvSignalHistory);
		mListView.setAdapter(mCustomCursorAdapter);

	}

	/**
	 * List the periods when the signal strength was the given category.
	 * 
	 * @param strength
	 *            the {@link SignalStrengthEnum} value for which the history is desired.
	 * @return A {@code Cursor} to the periods.
	 */
	private Cursor listSignalPeriods(SignalStrengthEnum strength) {
		String[] projection = { 
				TableSigStrength._ID,
				TableSigStrength.COLUMN_PREVIOUS_TIMESTAMP,
				TableSigStrength.COLUMN_TIMESTAMP 
			};
		String selection = TableSigStrength.COLUMN_PREVIOUS_SIGNAL_STRENGTH_LEVEL
				+ " LIKE ?";
		String[] selectionArgs = { SignalStrengthEnum.VERY_POOR.toString() };
		String sortOrder = TableSigStrength.COLUMN_TIMESTAMP + " ASC";
		return getContentResolver().query(TableSigStrength.CONTENT_URI,
				projection, selection, selectionArgs, sortOrder);
	}
}