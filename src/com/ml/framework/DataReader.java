package com.ml.framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {
	
	private ArrayList<String> dataStrings = new ArrayList<String>();
	
	public DataReader() {
		
	}
	
	public TrainingSet readDataSet(String path, int ioSeparation) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		int length = lines(br);
		FileReader fr2 = new FileReader(path);
		BufferedReader br2 = new BufferedReader(fr2);
		String[] lines = new String[length];
		for(int i = 0; i < length; i++) {
			lines[i] = br2.readLine();
		}
		br.close();
		br2.close();
		Vector[] inputs = new Vector[length];
		Vector[] outputs = new Vector[length];
		for(int i = 0; i < lines.length; i++) {
			inputs[i] = new Vector(ioSeparation);
			String[] stringInput = splitArray(lines[i].split(","), ioSeparation, 1);
			float[] floatInput = new float[stringInput.length];
			for(int inputIndex = 0; inputIndex < floatInput.length; inputIndex++) floatInput[inputIndex] = Float.parseFloat(stringInput[inputIndex]);
			inputs[i].set(floatInput);
			outputs[i] = new Vector(3);
			outputs[i].set(normalizeString(splitArray(lines[i].split(","), ioSeparation, 2)[0], 3));
		}
		return new TrainingSet(inputs, outputs);
	}
	
	public Vector readLtm(String path, int size) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		float[] ltm = new float[size];
		String[] splitString = br.readLine().split(",");
		for(int i = 0; i < splitString.length; i++) ltm[i] = Float.parseFloat(splitString[i]);
		br.close();
		return new Vector(ltm);
	}
	
	private int lines(BufferedReader br) throws IOException {
		int lines = 0;
		boolean reading = true;
		while (reading) {
			if (br.readLine() == null)
				reading = false;
			else
				lines++;
		}
		return lines;
	}
	
	private float[] normalizeString(String s, int numOptions) {
		if(!dataStrings.contains(s)) dataStrings.add(s);
		float[] result = new float[numOptions];
		for(int i = 0; i < result.length; i++) result[i] = 0;
		result[dataStrings.indexOf(s)] = 1;
		return result;
	}
	
	private String[] splitArray(String[] s, int index, int side) {
		String[] one = new String[index];
		String[] two = new String[s.length - index];
		for(int i = 0; i < one.length; i++) {
			one[i] = s[i];
		}
		for(int i = 0; i < two.length; i++) {
			two[i] = s[one.length + i];
		}
		if(side == 1) return one;
		else if(side == 2) return two;
		else return null;
	}
}
