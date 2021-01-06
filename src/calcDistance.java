
public class calcDistance {
	/*
	 * Method to calculate euclideanDistance
	 */	
	public static double euclidDistance(double[] point1, double[] point2) {
		double distance = 0;
		for (int feature=1;feature<point1.length;feature++) {
			distance += (point1[feature]-point2[feature])*(point1[feature]-point2[feature]);
		}
		distance = Math.sqrt(distance);
		return distance;
	}
	
	/*
	 * Method to calculate Hamming distance
	 */
	public static double hammingDistance(double[] point1, double[] point2) {
		double distance = 0;
		for (int feature=1;feature<point1.length;feature++) {
			if (point1[feature] != point2[feature]) {
				distance++;
			}
		}
		return distance;
	}
	
	/* 
	 * Method to calculate distance between two points when features are a mix of continuous and categorical data
	 * categoricalFeatures is an integer array specifying which features are categorical, all else 
	 */
	public static double mixedDistance(double[] point1, double[] point2, int[] categoricalFeatures) {
		double eDistance = 0;
		double hammingPoints = 0;
		for (int feature=1;feature<point1.length;feature++) {
			
			//If feature is categorical, use hamming distance
			if(arrayContains(categoricalFeatures,feature)) {
				if (point1[feature] != point2[feature]) {
					hammingPoints++;
				}
			}
			//Else, use euclidean distance
			else {
				eDistance += (point1[feature]-point2[feature])*(point1[feature]-point2[feature]);
			}
		}
		eDistance = Math.sqrt(eDistance);
		return eDistance + hammingPoints;
	}
	
	/*
	 * Method to check if an integer array contains a specified integer
	 */
	public static boolean arrayContains(int[] array, int toCheck) {
		for (int index=0;index<array.length;index++) {
			if (array[index]==toCheck) {
				return true;
			}
		}
		return false;
	}
}
