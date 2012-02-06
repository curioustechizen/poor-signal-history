
package in.kmb.poorsig;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The {@code ContentProvider} implementation.
 * 
 * @author Kiran Rao
 */
public class SignalStrengthProvider extends ContentProvider {

	/**
	 * The name of the SQLite Database.
	 */
	private static final String DATABASE_NAME = "poorsig.db";
	
	/**
	 * The DB version.
	 */
	private static final int DATABASE_VERSION = 1;
	
	
	public static final String SCHEME_CONTENT_URI = "content://";
	public static final String PROVIDER_AUTHORITY = "in.kmb.poorsig.sigprovider";
	private DatabaseHelper mOpenHelper;
	
	/**
	 * The code for signals
	 */
	private static final int SIGNALS = 0;
	
	/**
	 * The code for a particular signal
	 */
	private static final int SIGNAL_ID = 1;

	private static final UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(PROVIDER_AUTHORITY, "signal", SIGNALS);
		sUriMatcher.addURI(PROVIDER_AUTHORITY, "signal/#", SIGNAL_ID);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.delete(TableSigStrength.TABLE_NAME, selection, selectionArgs);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case SIGNAL_ID:
			return TableSigStrength.CONTENT_ITEM_TYPE;
		case SIGNALS:
			return TableSigStrength.CONTENT_TYPE;
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long insertedId = db.insert(TableSigStrength.TABLE_NAME, null, values);
		return ContentUris.withAppendedId(uri, insertedId);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		return db.query(TableSigStrength.TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * The Class DatabaseHelper.
	 */
	static class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * Instantiates a new database helper.
		 * 
		 * @param context
		 *            the context
		 */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TableSigStrength.SQL_CREATE_TABLE);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * The inner class for the SigStrength table.
	 */
	public static final class TableSigStrength implements BaseColumns {

		/**
		 * The name of the table.
		 */
		static final String TABLE_NAME = "SignalStrength";

		
		private TableSigStrength() {
			// DO not allow instantiation
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME_CONTENT_URI
				+ PROVIDER_AUTHORITY + "/signal");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of tags.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.uqn.sigstrength";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * tag.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.uqn.sigstrength";

		/**
		 * Name of "timestamp" column.
		 */
		public static final String COLUMN_TIMESTAMP = "timestamp";
		
		
		/**
		 * Name of "previous timestamp" column.
		 */
		public static final String COLUMN_PREVIOUS_TIMESTAMP = "prev_timestamp";
		
		/**
		 * Name of "previous signal level" column.
		 */
		public static final String COLUMN_PREVIOUS_SIGNAL_STRENGTH_LEVEL = "prevsignal_strength_level";

		/**
		 * SQL for creating the table.
		 */
		static final String SQL_CREATE_TABLE = 
				"CREATE TABLE " 
				+ TABLE_NAME + "( " 
					+ _ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_TIMESTAMP + " INTEGER, " 
					+ COLUMN_PREVIOUS_TIMESTAMP + " INTEGER, " 
					+ COLUMN_PREVIOUS_SIGNAL_STRENGTH_LEVEL+ " TEXT "
				+ ");";

	}

}
