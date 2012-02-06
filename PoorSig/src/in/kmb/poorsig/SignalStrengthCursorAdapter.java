
package in.kmb.poorsig;

import in.kmb.poorsig.SignalStrengthProvider.TableSigStrength;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * The custom {@code CursorAdapter} that exposes data from our poor history to the list view..
 * 
 * @author Kiran Rao
 */
public class SignalStrengthCursorAdapter extends CursorAdapter {
	
	private Context mCtx;
	private LayoutInflater mLayoutInflater;
	private int fromCol, toCol;
	

	/**
	 * Instantiates a new signal strength cursor adapter.
	 * 
	 * @param context
	 *            {@code Context} to work with
	 * @param c
	 *            {@code Cursor} for this Adapter.
	 */
	public SignalStrengthCursorAdapter(Context context, Cursor c) {
		super(context, c, false);
		this.mCtx = context;
		mLayoutInflater = LayoutInflater.from(mCtx);
		fromCol = c.getColumnIndex(TableSigStrength.COLUMN_PREVIOUS_TIMESTAMP);
		toCol = c.getColumnIndex(TableSigStrength.COLUMN_TIMESTAMP);
		
	}
	
	 

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tvFrom = (TextView) view.findViewById(R.id.textView1);
		TextView tvTo = (TextView) view.findViewById(R.id.textView3);
		
		tvFrom.setText(DateFormat.format("MM/dd h:mmaa", cursor.getLong(fromCol)));
		tvTo.setText(DateFormat.format("MM/dd h:mmaa", cursor.getLong(toCol)));

	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.list_item, parent, false);
	}

}
