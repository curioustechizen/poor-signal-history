/*
 * Kiran Rao
 */
package in.kmb.poorsig;

/**
 * The Enum representing the "buckets" into which signal strengths can be categorized.
 * 
 * @author Kiran Rao
 */
public enum SignalStrengthEnum {
	
	/**
	 * Very Poor strength.
	 */
	VERY_POOR(Constants.VERY_POOR_MIN, Constants.VERY_POOR_MAX, "VERY_POOR"),
	
	/**
	 * Poor strength.
	 */
	POOR(Constants.POOR_MIN, Constants.POOR_MAX, "POOR"),
	
	/**
	 * Normal strength.
	 */
	NORMAL(Constants.NORMAL_MIN, Constants.NORMAL_MAX, "NORMAL"),
	
	/**
	 * Good strength.
	 */
	GOOD (Constants.GOOD_MIN, Constants.GOOD_MAX, "GOOD"),
	
	/**
	 * Very Good strength.
	 */
	VERY_GOOD (Constants.VERY_GOOD_MIN, Constants.VERY_GOOD_MAX, "VERY_GOOD"),
	UNKNOWN(Constants.UNKNOWN, Constants.UNKNOWN, "UNKNOWN");
	
	/*
	 * The lower and upper bounds that each enum constants represents.
	 */
	private final int minStrength, maxStrength;
	private final String name;
	
	/**
	 * Instantiates a new signal strength enum.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @param name
	 *            the name
	 */
	SignalStrengthEnum(int min, int max, String name){
		this.minStrength = min;
		this.maxStrength = max;
		this.name = name;
	}
	
	/**
	 * Gets the min strength.
	 * 
	 * @return the min strength
	 */
	public int getMinStrength(){
		return this.minStrength;
	}
	
	/**
	 * Gets the max strength.
	 * 
	 * @return the max strength
	 */
	public int getMaxStrength(){
		return this.maxStrength;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
