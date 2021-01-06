import java.util.*;
import java.io.*;

public class knn {
	
	private ArrayList<double[]> trainedModel;
	
	public void knn() {}
	
	/*
	 * Performs naive training of a k-nearest neighbor model, simply copies input dataset as the trainedModel
	 */
	public void naiveTrain(ArrayList<double[]> trainingData) {
		this.trainedModel=trainingData;
	}
	
	/*
	 * A method for training k-nearest neighbors using condensed kNN training on classification problems
	 * Inputs are appropriately named, updates the non-static trained model
	 */
	public void condensedTrainClassify(ArrayList<double[]> trainingData, String distanceMethod, int[] categoricalFeatures) {
		System.out.println("------CONDENSED TRAINING-----");
		ArrayList<double[]> currentModel = new ArrayList<double[]>();
		Collections.shuffle(trainingData);
		currentModel.add(trainingData.get(0));
		for(int sample=1;sample<trainingData.size();sample++) {
			System.out.println("Prediction for: " + trainingData.get(sample)[0]);
			int predictedClass = knnClassifyPoint(currentModel,trainingData.get(sample),1,distanceMethod,categoricalFeatures);
			System.out.println("PREDICTED: " + predictedClass + ", ACTUAL: " + trainingData.get(sample)[0]);
			if (predictedClass != (int)trainingData.get(sample)[0]) {
				System.out.println("Sample misclassifed; added to condensed kNN model\n");
				currentModel.add(trainingData.get(sample));
			}
		}
		this.trainedModel = currentModel;
	}
	
	/*
	 * A method for training k-nearest neighbors using condensed kNN training on classification problems
	 * Inputs are appropriately named, updates the non-static trained model
	 */
	public void condensedTrainRegression(ArrayList<double[]> trainingData, double epsilon, String distanceMethod, double spread, int[] categoricalFeatures) {
		System.out.println("------CONDENSED TRAINING-----");
		ArrayList<double[]> currentModel = new ArrayList<double[]>();
		Collections.shuffle(trainingData);
		currentModel.add(trainingData.get(0));
		for(int sample=1;sample<trainingData.size();sample++) {
			System.out.println("Prediction for: " + trainingData.get(sample)[0]);
			double prediction = knnRegressPoint(currentModel,trainingData.get(sample),1,spread,distanceMethod,categoricalFeatures);
			System.out.println("PREDICTED: " + prediction + ", ACTUAL: " + trainingData.get(sample)[0]);
			if (epsilon < Math.abs(prediction-trainingData.get(sample)[0])) {
				System.out.println("Sample misclassifed; added to condensed kNN model\n");
				currentModel.add(trainingData.get(sample));
			}
		}
		this.trainedModel = currentModel;
	}
	
	/*
	 * A method for training k-nearest neighbors using edited kNN training, removes samples that are misclassified 
	 * Inputs are appropriately named, updates the non-static trained model
	 */
	public void editedTrainClassify(ArrayList<double[]> trainingData, String distanceMethod, int k, int[] categoricalFeatures) {
		System.out.println("------EDITED TRAINING-----");
		ArrayList<double[]> trainingModel = new ArrayList<double[]>(trainingData);
		Collections.shuffle(trainingData);
		boolean entryRemoved = true;
		while(entryRemoved) {
			int entriesRemoved=0;
			for(int index=0;index<trainingModel.size();index++) {
				ArrayList<double[]> tempArray=new ArrayList<double[]>(trainingModel);
				tempArray.remove(index);
				System.out.println("Prediction for: " + trainingModel.get(index)[0]);
				int predictedClass=knnClassifyPoint(tempArray,trainingModel.get(index),k,distanceMethod,categoricalFeatures);
				System.out.println("PREDICTED: " + predictedClass + ", ACTUAL: " + trainingModel.get(index)[0]);
				if ( (int) trainingModel.get(index)[0] != predictedClass) {
					System.out.println("Sample misclassifed; removed from edited kNN model\n");
					trainingModel.remove(index);
					index--;
					entriesRemoved++;
				}
			}
			if (entriesRemoved==0) {
				entryRemoved=false;
			}
			else {
				System.out.println("Rerunning editing...");
			}
		}
		this.trainedModel=trainingModel;
	}
	
	/*
	 * A method for training k-nearest neighbors using edited kNN training, removes samples that are misclassified
	 * Inputs are appropriately named, updates the non-static trained model
	 */
	public void editedTrainRegression(ArrayList<double[]> trainingData, double epsilon, String distanceMethod, int k, double spread, int[] categoricalFeatures) {
		System.out.println("------EDITED TRAINING-----");
		ArrayList<double[]> trainingModel = new ArrayList<double[]>(trainingData);
		Collections.shuffle(trainingData);
		boolean entryRemoved = true;
		while(entryRemoved) {
			int entriesRemoved=0;
			for(int index=0;index<trainingModel.size();index++) {
				ArrayList<double[]> tempArray=new ArrayList<double[]>(trainingModel);
				tempArray.remove(index);
				System.out.println("Prediction for: " + trainingModel.get(index)[0]);
				double prediction=knnRegressPoint(tempArray,trainingModel.get(index),k,spread,distanceMethod,categoricalFeatures);
				System.out.println("PREDICTED: " + (prediction) + ", ACTUAL: " + trainingModel.get(index)[0]);
				if (epsilon < Math.abs(prediction-trainingModel.get(index)[0])) {
					System.out.println("Sample misclassifed; removed from edited kNN model\n");
					trainingModel.remove(index);
					index--;
					entriesRemoved++;
				}
			}
			if (entriesRemoved==0) {
				entryRemoved=false;
			}
			else {
				System.out.println("Rerunning editing...");
			}
		}
		this.trainedModel=trainingModel;
	}
	
	/*
	 * Method to classify a series of samples using kNN
	 */
	public double[] knnClassifyData(ArrayList<double[]> data, int k, String distanceMethod, int[] categoricalFeatures, BufferedWriter buffWriter) throws IOException {
		int errors=0;
		for (double[] toClassify:data){
			System.out.println("Prediction for: " + toClassify[0]);
			int prediction = knnClassifyPoint(trainedModel,toClassify,k,distanceMethod,categoricalFeatures);
			if(prediction != (int) toClassify[0]) {
				errors++;
			}
			System.out.println("PREDICTED: " + (prediction) + ", ACTUAL: " + ((int) toClassify[0]) + "\n");
			//buffWriter.write("PREDICTED: class " + (prediction+1) + ", ACTUAL: " + ((int) toClassify[0]) + "\n");
		}
		double errorRate = (double)errors/(double)data.size();
		buffWriter.write("TOTAL MISCLASSIFICATIONS: " + errors + "\n");
		buffWriter.write("ERROR %: " + (errorRate*100) + "\n\n");
		return new double[]{errors,errorRate};
	}
	
	/*
	 * Method to regress a series of samples using kNN
	 */
	public double[] knnRegressData(ArrayList<double[]> data, double epsilon, int k, double spread, String distanceMethod, int[] categoricalFeatures, BufferedWriter buffWriter) throws IOException {
		double totalMSE = 0;
		int errors = 0;
		for (double[] toClassify:data){
			System.out.println("Prediction for: " + toClassify[0]);
			double prediction = knnRegressPoint(trainedModel,toClassify,k,spread,distanceMethod,categoricalFeatures);
			double difference = (toClassify[0]-prediction);
			if (difference>epsilon) {
				errors++;
			}
			totalMSE += difference*difference;
			System.out.println("PREDICTED: " + prediction + ", ACTUAL: " + toClassify[0] + "\n");
			//buffWriter.write("PREDICTED: " + prediction + ", ACTUAL: " + toClassify[0]);
		}
		double msePer = totalMSE/(double)data.size();
		buffWriter.write("WRONG PREDICTIONS: " + errors + "\n");
		buffWriter.write("TOTAL SE: " + totalMSE + "\n");
		buffWriter.write("MSE: " + msePer + "\n\n");
		
		return new double[]{(double) errors*100/data.size(),totalMSE,msePer};
	}
	
	/*
	 * A method to perform k-Nearest Neighbors classification on one point
	 * requires the training data, the query point, value for k, the method for calculating distance, and an array designating any categorical features for mixed distance
	 * 
	 * outputs the predicted class in integer form
	 */
	public static int knnClassifyPoint(ArrayList<double[]> trainingData, double[] queryPoint, int k, String distanceMethod, int[] categoricalFeatures) {
		//Identify kNN
		double[][] knn = identifyKNN(trainingData,queryPoint,k,distanceMethod,categoricalFeatures);
		//Create a hashmap/dictionary to store the counts for each class; key=class, value=class frequency in kNN
		HashMap<Integer,Integer> classCounts = new HashMap<Integer,Integer>();
		for(int neighbor=0;neighbor<knn.length;neighbor++) {
			System.out.println(trainingData.get((int)knn[neighbor][0])[0] + ", DISTANCE FROM: " + knn[neighbor][1]);
			int classAsInt = (int)trainingData.get((int)knn[neighbor][0])[0];
			if(knn[neighbor][1] < Double.MAX_VALUE && classCounts.get(classAsInt)==null) {
				classCounts.put(classAsInt,1);
			}
			else if (knn[neighbor][1] < Double.MAX_VALUE){
				classCounts.put(classAsInt,classCounts.get(classAsInt)+1);
			}
		}
		
		//Iterate over the map to find the plurality vote
		Map.Entry<Integer,Integer> predictedClass = null;
		for(Map.Entry<Integer,Integer> entry: classCounts.entrySet()) {
			if (predictedClass==null || predictedClass.getValue() < entry.getValue()) {
				predictedClass=entry;
			}
			//If there's a tie, then break the tie by finding the class with the least distance
			else if(predictedClass.getValue() == entry.getValue()) {
				double class1DistanceSum = 0;
				double class2DistanceSum = 0;
				//Sum the distances
				for(int neighbor=0;neighbor<knn.length;neighbor++) {
					if(knn[neighbor][0]==predictedClass.getKey()) {
						class1DistanceSum += knn[neighbor][1];
					}
					else if (knn[neighbor][0]==entry.getKey()) {
						class2DistanceSum += knn[neighbor][1];
					}
				}
				//update the predicted class if the new entry has lower distance sum
				if(class2DistanceSum<class1DistanceSum) {
					predictedClass=entry;
				}
			}
		}
		return predictedClass.getKey();
	}
	
	/*
	 * A method to perform k-Nearest Neighbors regression on one point
	 * requires the training data, the query point, value for k, value for spread, the method for calculating distance, and an array designating any categorical features for mixed distance
	 * Uses an RBF kernel to calculate point weighting
	 * 
	 * outputs the predicted value as a double
	 */
	public double knnRegressPoint(ArrayList<double[]> trainingData, double[] queryPoint, int k, double spread, String distanceMethod, int[] categoricalFeatures) {
		//Identify knn
		double[][] knn = identifyKNN(trainingData,queryPoint,k,distanceMethod,categoricalFeatures);
		double numerator=0;
		double denominator=0;
		double kernelVal=0;
		for (int index=0;index<knn.length;index++) {
			double[] neighbor = knn[index];
			if (neighbor[1]<Double.MAX_VALUE) {
				kernelVal = Math.exp(-1*(neighbor[1]*neighbor[1])/(2*spread*spread));
				numerator += kernelVal*trainingData.get((int) neighbor[0])[0];
				denominator += kernelVal;
			}
			System.out.println(trainingData.get((int) neighbor[0])[0] + ", DISTANCE FROM: " + neighbor[1] + ", KERNEL=" + kernelVal);
		}
		denominator+= Double.MIN_VALUE;
		return numerator/denominator;
	}
	
	/*
	 * Method to actually identify the k-nearest neighbors
	 */
	public static double[][] identifyKNN(ArrayList<double[]> trainingData, double[] queryPoint, int k, String distanceMethod, int[] categoricalFeatures){
		double[][] knn = new double[k][2];
		//Initialize all array entries to max value
		for(int index=0;index<knn.length;index++) {
			knn[index][1]=Double.MAX_VALUE;
		}
		//Iterate over training data
		for (int sample=0;sample<trainingData.size();sample++) {
			//Calculate distance according to specified method
			double distance;
			switch(distanceMethod) {
			case "euclidean":
				distance=calcDistance.euclidDistance(queryPoint, trainingData.get(sample));
				break;
			case "hamming":
				distance=calcDistance.hammingDistance(queryPoint, trainingData.get(sample));
				break;
			case "mixed":
				distance=calcDistance.mixedDistance(queryPoint, trainingData.get(sample), categoricalFeatures);
				break;
			default:
				throw new IllegalArgumentException("Unrecognized distance formula: " + distanceMethod);
			}
			
			//Check if this sample should be a new neighbor
			double[] newNeighbor = {(double) sample, distance};
			for(int index=0;index<knn.length;index++) {
				//If the distance is max value, then this spot is 'empty' and can simply be filled
				if(knn[index][1]==Double.MAX_VALUE) {
					knn[index]=newNeighbor;
					break;
				}
				//Else, we need to insert and shift the list
				else if(distance<knn[index][1]) {
					insertIntoKNN(newNeighbor,knn,index);
					break;
				}
			}
		}
		return knn;
	}
	
	/*
	 * Method to insert a new neighbor into the k-nearest neighbors list while maintaining an ascending distance ordering
	 */
	public static double[][] insertIntoKNN(double[] newNeighbor, double[][] knn, int atIndex){
		double[] toMove;
		double[] temp;
		toMove = clone(knn[atIndex]);
		knn[atIndex] = clone(newNeighbor);
		//Adjust the list
		for(int index=atIndex+1;index<knn.length;index++) {
			temp = clone(knn[index]);
			knn[index]=clone(toMove);
			toMove = clone(temp);
		}
		return knn;
	}
	
	/*
	 * Method to clone an array (desireable when an array needs to be copied, not the reference)
	 */
	public static double[] clone(double[] source) {
		double[] dest = new double[source.length];
		for (int index=0;index<source.length;index++) {
			dest[index]=source[index];
		}
		return dest;
	}
}
