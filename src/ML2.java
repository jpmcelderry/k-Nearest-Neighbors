import java.util.*;
import java.io.*;

public class ML2 {
	/*
	 * Driver
	 */
	public static void main(String[] args) throws Exception {
		double[][] glassData = null;
		double[][] segmentationData = null;
		double[][] votesData = null;
		double[][] abaloneData = null;
		double[][] machineData = null;
		double[][] firesData = null;
		for (int index=0;index<args.length;index++) {
			switch(args[index]) {
			case "glass.data":
				glassData = readData.readData(args[index]);
				break;
			case "segmentation.data":
				segmentationData = readData.readData(args[index]);
				break;
			case "house-votes-84.data":
				votesData = readData.readData(args[index]);
				break;
			case "abalone.data":
				abaloneData = readData.readData(args[index]);
				break;
			case "machine.data":
				machineData = readData.readData(args[index]);
				break;
			case "forestfires.csv":
				firesData = readData.readData(args[index]);
				break;
			default:
				throw new IllegalArgumentException("Unrecognized file:" + args[index]);
			}
		}
		/*
		double[][] dataSetToVerify = machineData;
		for(int sample=0;sample<toVerify.length;sample++) {
			for(int feature=0;feature<toVerify[sample].length;feature++) {
				System.out.print(toVerify[sample][feature] + " ");
			}
			System.out.println();
		}*/
		 
		/*
		//Votes Data
		BufferedWriter buffWriterVotes = new BufferedWriter(new FileWriter("votes-out.txt"));
		ArrayList<ArrayList<double[]>> votes = partitionData.partData(votesData,true);
		runClassifier(votes,3,"hamming",new int[]{-1},buffWriterVotes);
		buffWriterVotes.close();
		
		//Image Segmentation Data
		BufferedWriter buffWriterImages = new BufferedWriter(new FileWriter("segmentation-out.txt"));
		ArrayList<ArrayList<double[]>> images = partitionData.partData(segmentationData,true);
		runClassifier(images,2,"euclidean",new int[]{-1},buffWriterImages);
		buffWriterImages.close();
		*/
		//Glass Data
		BufferedWriter buffWriterGlass = new BufferedWriter(new FileWriter("glass-out.txt"));
		ArrayList<ArrayList<double[]>> glass = partitionData.partData(glassData,true);
		runClassifier(glass,1,"euclidean",new int[]{-1},buffWriterGlass);
		buffWriterGlass.close();
		/*
		//Abalone Data
		BufferedWriter buffWriterAbalone = new BufferedWriter(new FileWriter("abalone-out.txt"));
		ArrayList<ArrayList<double[]>> abalone = partitionData.partData(abaloneData,false);
		runRegressor(abalone,1,3,2,"mixed",new int[]{1},buffWriterAbalone);
		buffWriterAbalone.close();
		
		//Fires Data
		BufferedWriter buffWriterFires = new BufferedWriter(new FileWriter("fires-out.txt"));
		ArrayList<ArrayList<double[]>> fires = partitionData.partData(firesData,false);
		runRegressor(fires,0.25,2,5,"mixed",new int[]{3,4},buffWriterFires);
		buffWriterFires.close();
		*/
		//Machines Data
		BufferedWriter buffWriterMachine = new BufferedWriter(new FileWriter("machine-out.txt"));
		ArrayList<ArrayList<double[]>> machine = partitionData.partData(machineData,false);
		runRegressor(machine,25,3,1000,"mixed",new int[]{1},buffWriterMachine);
		buffWriterMachine.close();

	}
	
	/*
	 * Procedure for classification; performs 5-fold cross validation, saves output, writes output 
	 */
	public static void runClassifier(ArrayList<ArrayList<double[]>> dataSets, int k,String distanceMethod,int[] categoricalFeatures,BufferedWriter buffWriter) throws IOException {
		double[][] naiveError = new double[5][2];
		double[][] editedError = new double[5][2];
		double[][] condensedError = new double[5][2];
		double naiveTime = 0;
		double editedTime = 0;
		double condensedTime = 0;
		long start;
		long stop;
		
		for(int holdSet=0;holdSet<5;holdSet++) {
			ArrayList<double[]> trainSet = new ArrayList<double[]>();
			//Compile all the non-holdSets together
			for(int set=0;set<5;set++) {
				if(set != holdSet) {	
					trainSet.addAll(dataSets.get(set));
				}
			}
			buffWriter.write("--------------HOLD OUT SET: " + (holdSet+1) + "---------------\n");
			
			//Naive training/analysis with given hold-out set
			buffWriter.write("Naive Training\n");
			knn naive = new knn();
			start = System.nanoTime();
			naive.naiveTrain(trainSet);
			stop = System.nanoTime();
			naiveTime += stop-start;
			double[] naiveErrorTemp = naive.knnClassifyData(dataSets.get(holdSet),k,distanceMethod,categoricalFeatures,buffWriter);
			
			naiveError[holdSet]=naiveErrorTemp;
			
			//Edited training/analysis with given hold-out set
			buffWriter.write("Edited Training\n");
			knn edited = new knn();
			start = System.nanoTime();
			edited.editedTrainClassify(trainSet,distanceMethod,k,categoricalFeatures);
			double[] editedErrorTemp = edited.knnClassifyData(dataSets.get(holdSet),k,distanceMethod,categoricalFeatures,buffWriter);
			stop = System.nanoTime();
			editedTime += stop-start;
			editedError[holdSet]=editedErrorTemp;
			
			//Condensed training with given hold-out set
			buffWriter.write("Condensed Training\n");
			knn condensed = new knn();
			start = System.nanoTime();
			condensed.condensedTrainClassify(trainSet,distanceMethod,categoricalFeatures);
			double[] condensedErrorTemp = condensed.knnClassifyData(dataSets.get(holdSet),k,distanceMethod,categoricalFeatures,buffWriter);
			stop = System.nanoTime();
			condensedTime += stop-start;
			condensedError[holdSet]=condensedErrorTemp;
			
			System.out.println("run complete");
		}
		
		//Calculate average results
		double naiveFinalResults= 0;
		double editedFinalResults = 0;
		double condensedFinalResults = 0;
		for(int index=0;index<5;index++) {
			naiveFinalResults += naiveError[index][1];
			editedFinalResults += editedError[index][1];
			condensedFinalResults += condensedError[index][1];
		}
		//write output
		buffWriter.write("AVG NAIVE ERROR RATE: " + (naiveFinalResults*100.0/5.0) + "\n");
		buffWriter.write("AVG EDITED ERROR RATE: " + (editedFinalResults*100.0/5.0) + "\n");
		buffWriter.write("AVG CONDENSED ERROR RATE: " + (condensedFinalResults*100.0/5.0) + "\n");
		buffWriter.write("----TIME TAKEN (ns)----:\nNAIVE: " + naiveTime + "\nEDITED: " + editedTime + "\nCONDENSED: " + condensedTime);
	}
	
	/*
	 * Procedure for regression; performs 5-fold cross validation, saves output, writes output 
	 */
	public static void runRegressor(ArrayList<ArrayList<double[]>> dataSets, double epsilon, int k, double spread, String distanceMethod,int[] categoricalFeatures,BufferedWriter buffWriter) throws IOException {
		double[][] naiveError = new double[5][2];
		double[][] editedError = new double[5][2];
		double[][] condensedError = new double[5][2];
		double naiveTime = 0;
		double editedTime = 0;
		double condensedTime = 0;
		long start;
		long stop;
		
		for(int holdSet=0;holdSet<5;holdSet++) {
			ArrayList<double[]> trainSet = new ArrayList<double[]>();
			//Compile all the non-holdSets together
			for(int set=0;set<5;set++) {
				if(set != holdSet) {	
					trainSet.addAll(dataSets.get(set));
				}
			}
			buffWriter.write("--------------HOLD OUT SET: " + (holdSet+1) + "---------------\n");
			
			//Perform Naive Training/Analysis for a given holdSet
			buffWriter.write("Naive Training\n");
			knn naive = new knn();
			start = System.nanoTime();
			naive.naiveTrain(trainSet);
			double[] naiveErrorTemp = naive.knnRegressData(dataSets.get(holdSet),epsilon,k,spread,distanceMethod,categoricalFeatures,buffWriter);
			stop = System.nanoTime();
			naiveTime += stop-start;
			naiveError[holdSet]=naiveErrorTemp;
			
			//Perform Edited Training/Analysis for a given holdSet
			buffWriter.write("Edited Training\n");
			knn edited = new knn();
			start = System.nanoTime();
			edited.editedTrainRegression(trainSet,epsilon,distanceMethod,k,spread,categoricalFeatures);
			double[] editedErrorTemp = edited.knnRegressData(dataSets.get(holdSet),epsilon,k,spread,distanceMethod,categoricalFeatures,buffWriter);
			stop = System.nanoTime();
			editedTime += stop-start;
			editedError[holdSet]=editedErrorTemp;
			
			//Perform Condensed Training/Analysis for a given holdSet
			buffWriter.write("Condensed Training\n");
			knn condensed = new knn();
			start = System.nanoTime();
			condensed.condensedTrainRegression(trainSet,epsilon,distanceMethod,spread,categoricalFeatures);
			double[] condensedErrorTemp = condensed.knnRegressData(dataSets.get(holdSet),epsilon,k,spread,distanceMethod,categoricalFeatures,buffWriter);
			stop = System.nanoTime();
			condensedTime += stop-start;
			condensedError[holdSet]=condensedErrorTemp;
			
			System.out.println("run complete");
		}
		
		//Calculate mean results
		double[] naiveFinalResults= new double[2];
		double[] editedFinalResults = new double[2];
		double[] condensedFinalResults = new double[2];
		for(int index=0;index<5;index++) {
			naiveFinalResults[0] += naiveError[index][0];
			editedFinalResults[0] += editedError[index][0];
			condensedFinalResults[0] += condensedError[index][0];
			naiveFinalResults[1] += naiveError[index][2];
			editedFinalResults[1] += editedError[index][2];
			condensedFinalResults[1] += condensedError[index][2];
		}
		
		//write output
		buffWriter.write("NAIVE MISCLASSIFICATIONS RATE: " + (naiveFinalResults[0]/5.0) + "%\n");
		buffWriter.write("NAIVE MSE: " + (naiveFinalResults[1]/5.0) + "\n");
		buffWriter.write("EDITED MISCLASSIFICATION RATE: " + (editedFinalResults[0]/5.0) + "%\n");
		buffWriter.write("EDITED MSE: " + (editedFinalResults[1]/5.0) + "\n");
		buffWriter.write("CONDENSED MISCLASSIFICATIONS RATE: " + (condensedFinalResults[0]/5.0) + "%\n");
		buffWriter.write("CONDENSED MSE: " + (condensedFinalResults[1]/5.0) + "\n");
		buffWriter.write("----TIME TAKEN (ns)----:\nNAIVE: " + naiveTime + "\nEDITED: " + editedTime + "\nCONDENSED: " + condensedTime);
	}
	
}