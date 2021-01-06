import java.util.*;
import java.io.*;

public class readData {
	
	public static double[][] readData(String file) throws Exception{
		BufferedReader buffReader = null;
		try {
			String currentLine;
			String sample[];
			String[][] stringArray = initiateStringArray(file);
			buffReader = new BufferedReader(new FileReader(file));
			int lineCounter = 0;
			//Read file to string array
			while((currentLine=buffReader.readLine()) != null) {	//read until end of file
				if(!currentLine.trim().equals("")) {	//don't read empty lines
					sample = currentLine.trim().split(",");
					for (int index=0;index<sample.length;index++) {
						stringArray[lineCounter][index] = sample[index];
					}
					lineCounter++;
				}
			}
			//Use different methods to parse each input dataset
			switch(file) {
			case "glass.data":
				return parseGlass(stringArray);
			case "segmentation.data":
				return parseSegmentation(stringArray);
			case "house-votes-84.data":
				return parseVotes(stringArray);
			case "abalone.data":
				return parseAbalone(stringArray);
			case "machine.data":
				return parseMachine(stringArray);
			case "forestfires.csv":
				return parseFires(stringArray);
			default:
				throw new IllegalArgumentException("Unrecognized file: " + file);
			}
		}
		finally {
			if(buffReader != null) {buffReader.close();}
		}
	}
	
	/*
	 * Provides a string array of appropriate size to hold data when reading from file
	 */
	public static String[][] initiateStringArray(String file) throws IllegalArgumentException {
		switch(file) {
		case "glass.data":
			return new String[214][11];
		case "segmentation.data":
			return new String[2310][20];
		case "house-votes-84.data":
			return new String[435][17];
		case "abalone.data":
			return new String[4177][9];
		case "machine.data":
			return new String[209][10];
		case "forestfires.csv":
			return new String[517][13];
		default:
			throw new IllegalArgumentException("Invalid file: " + file);
		}
	}
	
	/*
	 * The following are methods for taking input String[][] datasets and parsing them to create double[][] arrays, which can be worked with
	 * 
	 * Note that all categorical data has been transformed to an integer (which must be represented as a double)
	 */
	public static double[][] parseGlass(String[][] stringArray){
		double[][] data = new double[214][10];
		for (int sampleIndex = 0;sampleIndex<stringArray.length;sampleIndex++) {
			data[sampleIndex][0] = Double.parseDouble(stringArray[sampleIndex][stringArray[sampleIndex].length-1])-1;
			for (int feature=1;feature<stringArray[sampleIndex].length-1;feature++) {
				data[sampleIndex][feature]=Double.parseDouble(stringArray[sampleIndex][feature]);
			}
		}
		return data;
	}
	
	public static double[][] parseSegmentation(String[][] stringArray){
		double[][] data = new double[2310][20];
		for (int sampleIndex = 0;sampleIndex<stringArray.length;sampleIndex++) {
			switch(stringArray[sampleIndex][0]) {
			case "BRICKFACE":
				data[sampleIndex][0] = 0;
				break;
			case "SKY":
				data[sampleIndex][0] = 1;
				break;
			case "FOLIAGE":
				data[sampleIndex][0] = 2;
				break;
			case "CEMENT":
				data[sampleIndex][0] = 3;
				break;
			case "WINDOW":
				data[sampleIndex][0] = 4;
				break;
			case "PATH":
				data[sampleIndex][0] = 5;
				break;
			case "GRASS":
				data[sampleIndex][0] = 6;
				break;
			default:
				data[sampleIndex][0] = 7;
				break;
			}
			for (int feature=1;feature<stringArray[sampleIndex].length;feature++) {
				data[sampleIndex][feature]=Double.parseDouble(stringArray[sampleIndex][feature]);
			}
		}
		return data;
	}
	
	public static double[][] parseVotes(String[][] stringArray){
		double[][] data = new double[435][17];
		for (int sampleIndex = 0;sampleIndex<stringArray.length;sampleIndex++) {
			//Determine class of sample
			if (stringArray[sampleIndex][0].equals("democrat")) {
				data[sampleIndex][0]=1;
			}
			else {
				data[sampleIndex][0]=0;
			}
			for (int feature=1;feature<stringArray[sampleIndex].length;feature++) {
				if (stringArray[sampleIndex][feature].equals("y")) {
					data[sampleIndex][feature]=0;
				}
				else if (stringArray[sampleIndex][feature].equals("n")) {
					data[sampleIndex][feature]=1;
				}
				else {
					data[sampleIndex][feature]=2;
				}
			}
		}
		return data;
	}
	
	public static double[][] parseAbalone(String[][] stringArray){
		double[][] data = new double[4177][9];
		for (int sampleIndex = 0;sampleIndex<stringArray.length;sampleIndex++) {
			data[sampleIndex][0] = Double.parseDouble(stringArray[sampleIndex][stringArray[sampleIndex].length-1]);
			switch(stringArray[sampleIndex][0]) {
			case "I":
				data[sampleIndex][1] = 0;
				break;
			case "M":
				data[sampleIndex][1] = 1;
				break;
			case "F":
				data[sampleIndex][1] = 2;
				break;
			}
			for (int feature=1;feature<stringArray[sampleIndex].length-1;feature++) {
				data[sampleIndex][feature]=Double.parseDouble(stringArray[sampleIndex][feature+1]);
			}
		}
		return data;
	}
	
	public static double[][] parseMachine(String[][] stringArray){
		double[][] data = new double[209][8];
		for (int sampleIndex = 0;sampleIndex<stringArray.length;sampleIndex++) {
			data[sampleIndex][0] = Double.parseDouble(stringArray[sampleIndex][stringArray[sampleIndex].length-2]);
			switch(stringArray[sampleIndex][0]) {
			case "adviser":
				data[sampleIndex][1]=0;
				break;
			case "amdahl":
				data[sampleIndex][1]=1;
				break;
			case "apollo":
				data[sampleIndex][1]=2;
				break;
			case "basf":
				data[sampleIndex][1]=3;
				break;
			case "bti":
				data[sampleIndex][1]=4;
				break;
			case "burroughs":
				data[sampleIndex][1]=5;
				break;
			case "c.r.d":
				data[sampleIndex][1]=6;
				break;
			case "cdc":
				data[sampleIndex][1]=7;
				break;
			case "cambex":
				data[sampleIndex][1]=8;
				break;
			case "dec":
				data[sampleIndex][1]=9;
				break;
			case "dg":
				data[sampleIndex][1]=10;
				break;
			case "formation":
				data[sampleIndex][1]=11;
				break;
			case "four-phase":
				data[sampleIndex][1]=12;
				break;
			case "gould":
				data[sampleIndex][1]=13;
				break;
			case "hp":
				data[sampleIndex][1]=14;
				break;
			case "harris":
				data[sampleIndex][1]=15;
				break;
			case "honeywell":
				data[sampleIndex][1]=16;
				break;
			case "ibm":
				data[sampleIndex][1]=17;
				break;
			case "ipl":
				data[sampleIndex][1]=18;
				break;
			case "magnuson":
				data[sampleIndex][1]=19;
				break;
			case "microdata":
				data[sampleIndex][1]=20;
				break;
			case "nas":
				data[sampleIndex][1]=21;
				break;
			case "ncr":
				data[sampleIndex][1]=22;
				break;
			case "nixdorf":
				data[sampleIndex][1]=23;
				break;
			case "perkin-elmer":
				data[sampleIndex][1]=24;
				break;
			case "prime":
				data[sampleIndex][1]=25;
				break;
			case "siemens":
				data[sampleIndex][1]=26;
				break;
			case "sperry":
				data[sampleIndex][1]=27;
				break;
			case "sratus":
				data[sampleIndex][1]=28;
				break;
			case "wang":
				data[sampleIndex][1]=29;
				break;
			}
			for(int feature=2;feature<stringArray[sampleIndex].length-2;feature++) {
				data[sampleIndex][feature]=Double.parseDouble(stringArray[sampleIndex][feature]);
			}
		}
		return data;
	}
	
	public static double[][] parseFires(String[][] stringArray){
		double[][] data = new double[517][13];
		for (int sampleIndex = 0;sampleIndex<stringArray.length;sampleIndex++) {
			data[sampleIndex][0] = Math.log(Double.parseDouble(stringArray[sampleIndex][stringArray[sampleIndex].length-1])+1);	//The fire area was log-transformed using the suggested formula ln(x+1)
			for (int feature=0;feature<stringArray[sampleIndex].length-1;feature++) {
				if (feature==2) {
					switch(stringArray[sampleIndex][feature]) {
					case "jan":
						data[sampleIndex][feature+1] = 1;
						break;
					case "feb":
						data[sampleIndex][feature+1] = 2;
						break;
					case "mar":
						data[sampleIndex][feature+1] = 3;
						break;
					case "apr":
						data[sampleIndex][feature+1] = 4;
						break;
					case "may":
						data[sampleIndex][feature+1] = 5;
						break;
					case "jun":
						data[sampleIndex][feature+1] = 6;
						break;
					case "jul":
						data[sampleIndex][feature+1] = 7;
						break;
					case "aug":
						data[sampleIndex][feature+1] = 8;
						break;
					case "sep":
						data[sampleIndex][feature+1] = 9;
						break;
					case "oct":
						data[sampleIndex][feature+1] = 10;
						break;
					case "nov":
						data[sampleIndex][feature+1] = 11;
						break;
					case "dec":
						data[sampleIndex][feature+1] = 12;
						break;
					}
				}
				else if (feature==3) {
					switch(stringArray[sampleIndex][feature]) {
					case "sun":
						data[sampleIndex][feature+1] = 1;
						break;
					case "mon":
						data[sampleIndex][feature+1] = 2;
						break;
					case "tue":
						data[sampleIndex][feature+1] = 3;
						break;
					case "wed":
						data[sampleIndex][feature+1] = 4;
						break;
					case "thu":
						data[sampleIndex][feature+1] = 5;
						break;
					case "fri":
						data[sampleIndex][feature+1] = 6;
						break;
					case "sat":
						data[sampleIndex][feature+1] = 7;
						break;
					}
				}
				else {
					data[sampleIndex][feature+1]=Double.parseDouble(stringArray[sampleIndex][feature]);
				}
			}
		}
		return data;
	}
}
