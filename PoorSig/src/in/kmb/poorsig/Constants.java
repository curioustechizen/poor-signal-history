
package in.kmb.poorsig;

/**
 * Collection of constants.
 * 
 * @author Kiran Rao
 */
public interface Constants {
	
	/**Lower bound for very poor strength.*/
	static final int VERY_POOR_MIN = 0;
	
	/**Upper bound for very poor strength.*/
	static final int VERY_POOR_MAX = 2;
	
	/**Lower bound for poor strength.*/
	static final int POOR_MIN = 3;
	
	/**Upper bound for poor strength.*/
	static final int POOR_MAX = 5;
	
	/**Lower bound for normal strength.*/
	static final int NORMAL_MIN = 6;
	
	/**Upper bound for normal strength.*/
	static final int NORMAL_MAX = 25;
	
	/**Lower bound for good strength.*/
	static final int GOOD_MIN = 26;
	
	/**Upper bound for good strength.*/
	static final int GOOD_MAX = 28;
	
	/**Lower bound for very good strength.*/
	static final int VERY_GOOD_MIN = 27;
	
	/**Upper bound for very good strength.*/
	static final int VERY_GOOD_MAX = 31;
	
	/**Represents unknown signal strength*/
	static final int UNKNOWN = 99;
	
	/**
	 * Key used to store the "first run" shared preference.
	 */
	static final String KEY_FIRST_RUN = "in.kmb.poorsig.FIRST_RUN";
	
	/** Tag for logging*/
	static final String TAG = "PoorSig";
}
