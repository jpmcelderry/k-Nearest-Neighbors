import java.util.*;
import java.io.*;

public class partitionData {
	 public static ArrayList<ArrayList<double[]>> partData(double[][] data, boolean classificationProblem) {
		 ArrayList<double[]> trainingSet = new ArrayList<double[]>();
		 ArrayList<double[]> fold1 = new ArrayList<double[]>();
		 ArrayList<double[]> fold2 = new ArrayList<double[]>();
		 ArrayList<double[]> fold3 = new ArrayList<double[]>();
		 ArrayList<double[]> fold4 = new ArrayList<double[]>();
		 ArrayList<double[]> fold5 = new ArrayList<double[]>();
		 
		 //Turn array into arraylist
		 ArrayList<double[]> temp = new ArrayList<double[]>();
		 for(double[] sample:data) {
			 temp.add(sample);
		 }
		 
		 //Regression problems need to be sorted first
		 if(!classificationProblem) {
			Collections.sort(temp, new Comparator<double[]>() {
			 	public int compare(double[] row1, double[] row2) {
			 		return Double.compare(row1[0],row2[0]);
			 	}
			 });
		 }
		 
		 //First, remove to trainingSet (this is before shuffling a classification problem to keep the training dataset consistent)
		 for(int index=temp.size()-1;index>=0;index--) {
			 if(index != 0 && index%10==0) {
				 trainingSet.add(temp.remove(index));
			 }
		 }
		 
		 //Classification problems are shuffled, then sorted preserving the random order
		 if(classificationProblem) {
		 	Collections.shuffle(temp);
		 	Collections.sort(temp, new Comparator<double[]>() {
		 		public int compare(double[] row1, double[] row2) {
		 			return Double.compare(row1[0],row2[0]);
		 		}
		 	});
		 }
		 
		 //remove every 1 in 5 consecutive samples to each fold
		 for(int index=temp.size()-1;index>=0;index--) {
			 switch(index%5) {
			 	case 0:
			 		fold1.add(temp.remove(index));
			 		break;
			 	case 1:
			 		fold2.add(temp.remove(index));
			 		break;
			 	case 2:
			 		fold3.add(temp.remove(index));
			 		break;
			 	case 3:
			 		fold4.add(temp.remove(index));
			 		break;
			 	case 4:
			 		fold5.add(temp.remove(index));
			 		break;	
			 }
		 }
		 
		 ArrayList<ArrayList<double[]>> returnData = new ArrayList<ArrayList<double[]>>();
		 returnData.add(fold1);
		 returnData.add(fold2);
		 returnData.add(fold3);
		 returnData.add(fold4);
		 returnData.add(fold5);
		 returnData.add(trainingSet);
		 
		 return returnData;
	 }
}
