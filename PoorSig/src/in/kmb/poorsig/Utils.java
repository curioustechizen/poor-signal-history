/*
 * Kiran Rao
 */
package in.kmb.poorsig;

/**
 * Collection of utility methods.
 * 
 * @author Kiran Rao
 */
public class Utils {

	/**
	 * Places the GSM signal strength into the appropriate "bucket" (Very Good, Poor etc).
	 * 
	 * @param gsmStrength
	 *            the GSM signal strength value
	 * @return the enum representing the "bucket" to which this signal strength should be placed.
	 */
	static SignalStrengthEnum getEnumForStrength(int gsmStrength) {

		SignalStrengthEnum returnValue = SignalStrengthEnum.UNKNOWN;
		if (gsmStrength == Constants.UNKNOWN) {
			returnValue = SignalStrengthEnum.UNKNOWN;
		}

		else if (gsmStrength >= Constants.VERY_POOR_MIN
				&& gsmStrength <= Constants.VERY_POOR_MAX) {
			returnValue = SignalStrengthEnum.VERY_POOR;
		}

		else if (gsmStrength >= Constants.POOR_MIN
				&& gsmStrength <= Constants.POOR_MAX) {
			returnValue = SignalStrengthEnum.POOR;
		}

		else if (gsmStrength >= Constants.NORMAL_MIN
				&& gsmStrength <= Constants.NORMAL_MAX) {
			returnValue = SignalStrengthEnum.NORMAL;
		}

		else if (gsmStrength >= Constants.GOOD_MIN
				&& gsmStrength <= Constants.GOOD_MAX) {
			returnValue = SignalStrengthEnum.GOOD;
		}

		else if (gsmStrength >= Constants.VERY_GOOD_MIN
				&& gsmStrength <= Constants.VERY_GOOD_MAX) {
			returnValue = SignalStrengthEnum.VERY_GOOD;
		}

		return returnValue;
	}

	/**
	 * Gets the enum corresponding to the "signal level" value. 
	 * This signal level is the number of bars that the phone displays. 
	 * 
	 * @param signalLevel
	 *            the signal level
	 * @return the enum for level
	 */
	public static SignalStrengthEnum getEnumForLevel(int signalLevel) {
		switch (signalLevel) {
		case 0:
			return SignalStrengthEnum.VERY_POOR;
			
		case 1:
			return SignalStrengthEnum.POOR;
			
		case 2:
			return SignalStrengthEnum.NORMAL;
			
		case 3:
			return SignalStrengthEnum.GOOD;
			
		case 4:
			return SignalStrengthEnum.VERY_GOOD;
			
		default:
			return SignalStrengthEnum.VERY_POOR;
			
		}
	}

}
