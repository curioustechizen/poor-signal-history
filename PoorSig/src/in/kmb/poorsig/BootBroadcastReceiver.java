
package in.kmb.poorsig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The {@code BroadcastReceiver} which is responsible for starting our background service as soon as the phone is booted.
 * 
 * @author Kiran Rao
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			Log.d(Constants.TAG, "Received "+ Intent.ACTION_BOOT_COMPLETED +"Starting SignalStrengthService");
			Intent startServiceIntent = new Intent(context, SignalStrengthService.class);
			context.startService(startServiceIntent);
		}

	}

}
