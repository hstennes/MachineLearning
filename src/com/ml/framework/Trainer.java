package com.ml.framework;

import java.io.IOException;
import java.util.Random;

import com.ml.main.MLExperiment;
import com.ml.model.MLAlgorithm;
import com.ml.model.Polynomial;

public class Trainer {
	
	private Random r;
	private ErrorCalculator errorCalculator;
	private MLAlgorithm algorithm;
	public TrainingSet trainingSet;
	private MLExperiment m;
	
	public Trainer(MLExperiment m) {
		r = new Random();
		errorCalculator = new ErrorCalculator();
		algorithm = m.getAlgorithm();
		trainingSet = new TrainingSet();
		this.m = m;
	}
	
	public void createPolyTrainingSet(int startInput, int size) {
		algorithm = m.getAlgorithm();
		Vector realCoeffs = algorithm.getLtm();
		Polynomial tempPoly = new Polynomial(realCoeffs.getLength() - 1, realCoeffs);
		Vector[] inputs = new Vector[size]; 
		Vector[] outputs = new Vector[size];
		for(int i = 0; i < size; i++) {
			inputs[i] = new Vector(new float[] {startInput + i});
			outputs[i] = new Vector(new float[] {tempPoly.evaluate(new Vector(new float[]{startInput + i})).get(0)});
		}
		trainingSet.setInputArray(inputs);
		trainingSet.setOutputArray(outputs);
	}
	
	public void createIrisTrainingSet() {
		try {
			trainingSet = m.getDataReader().readDataSet("data/IrisDataSetTraining.txt", 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createXORTrainingSet() {
		Vector[] input = new Vector[4];
		input[0] = new Vector(new float[] {0, 0});
		input[1] = new Vector(new float[] {0, 1});
		input[2] = new Vector(new float[] {1, 0});
		input[3] = new Vector(new float[] {1, 1});
		Vector[] output = new Vector[4];
		output[0] = new Vector(new float[] {0});
		output[1] = new Vector(new float[] {1});
		output[2] = new Vector(new float[] {1});
		output[3] = new Vector(new float[] {0});
		trainingSet = new TrainingSet(input, output);
	}
	
	public Vector trainGreedyRandom(int lowRange, int highRange, int iterations) {
		algorithm = m.getAlgorithm(); 
		Vector ltm = new Vector(algorithm.getLtm().getLength());
		for(int i = 0; i < ltm.getLength(); i++) ltm.set(i, (float) (lowRange + r.nextDouble() * (highRange - lowRange)));
		
		for(int i = 0; i < iterations; i++) {
			System.out.println("Iteration #" + i + " Score " + calculateScore(ltm));
			Vector newLtm = new Vector(algorithm.getLtm().getLength());
			for(int x = 0; x < ltm.getLength(); x++) newLtm.set(x, (float) (lowRange + r.nextDouble() * (highRange - lowRange)));
			if(calculateScore(newLtm) < calculateScore(ltm)) {
				ltm = newLtm;
			}
		}
		return ltm;
	}
	
	public Vector trainHillClimbing(float acceleration, float initialVelocity) {
		algorithm = m.getAlgorithm();
		int ltmLength = algorithm.getLtm().getLength();
		Vector ltm = new Vector(ltmLength);
		for(int i = 0; i < ltm.getLength(); i++) ltm.set(i, (float) (-20 + r.nextDouble() * (20 - (-20))));
		float[] stepSize = new float[ltmLength];
		for(int i = 0; i < stepSize.length; i++) stepSize[i] = initialVelocity;
		float[] candidate = new float[5];
		candidate[0] = -acceleration;
		candidate[1] = -1 / acceleration;
		candidate[2] = 0;
		candidate[3] = 1 / acceleration;
		candidate[4] = acceleration;
 		
		boolean improving = true;
		int iterations = 0;
		while(improving) {
			double oldScore = calculateScore(ltm);
			if(iterations % 100 == 0) {
				System.out.print("Iteration " + iterations);
				System.out.println(" with score " + oldScore);
			}
			for(int i = 0; i < ltmLength; i++) {
				int best = -1;
				double bestScore = Double.POSITIVE_INFINITY;
				for(int j = 0; j < candidate.length; j++) {
					ltm.set(i, ltm.get(i) + (stepSize[i] * candidate[j]));
					double temp = calculateScore(ltm);
					ltm.set(i, ltm.get(i) - (stepSize[i] * candidate[j])); 
					if(temp < bestScore) {
						bestScore = temp;
						best = j;
					}
				}
				if(best != -1) {
					ltm.set(i, ltm.get(i) + (stepSize[i] * candidate[best]));
				}
			}
			iterations++;
			if(calculateScore(ltm) == oldScore || iterations > 2000) improving = false;
		}
		return ltm;
	}
	
	public Vector trainSimulatedAnnealing(int cyclesPerIteration, int kMax, double startTemp, double endTemp) {
		double globalBestError = Double.POSITIVE_INFINITY;
		Vector globalBest = null;
		double currentTemp = startTemp;
		double currentError = Double.POSITIVE_INFINITY;
		
		algorithm = m.getAlgorithm();
		
		Vector ltm = new Vector(algorithm.getLtm().getLength());
		for(int i = 0; i < ltm.getLength(); i++) ltm.set(i, (float) (-20 + r.nextDouble() * (20 - (-20))));
		
		for(int k = 0; k < kMax; k++) {
			currentTemp = coolingSchedule(k, kMax, startTemp, endTemp);
			System.out.println("error " + currentError + " temp " + currentTemp);
			
			for(int cycle = 0; cycle < cyclesPerIteration; cycle++) {
				Vector oldState = (Vector) ltm.clone();
				ltm.set(performRandomize(ltm.getFloatArray()));
				double trialError = calculateScore(ltm);
				
				boolean keep = false;
				if(trialError < currentError) keep = true;
				else {
					double p = calcProbability(currentError, trialError, currentTemp);
					if(p > r.nextFloat()) keep = true;
				}
				
				if(keep) {
					currentError = trialError;
					if(trialError < globalBestError) {
						globalBestError = trialError;
						oldState = (Vector) ltm.clone();
						globalBest = (Vector) ltm.clone();
					}
				}
				else ltm = (Vector) oldState.clone();
			}
		}
		return globalBest;
	}
	
	public double coolingSchedule(int k, int kMax, double startingTemperature, double endingTemperature) {
        final double ex = (double) k / (double) kMax;
        return startingTemperature * Math.pow(endingTemperature / startingTemperature, ex);
	}
	
	public double calcProbability(final double ecurrent, final double enew, final double t) {
        return Math.exp(-(enew - ecurrent) / t);
    }
	
	public float[] performRandomize(final float[] memory) {
        for (int i = 0; i < memory.length; i++) {
            final double d = r.nextGaussian() / 10;
            memory[i] += d;
        }
        return memory;
    }
	
	public void refreshAlgorithm() {
		algorithm = m.getAlgorithm();
	}
	
	public double calculateScore(Vector ltm) {
		algorithm.setLtm(ltm);
		int trainingSetLength = trainingSet.getLength();
		int outputDimensions = trainingSet.getOutputDimensions();
		
		Vector ideal = new Vector(trainingSetLength * outputDimensions);
		for(int i = 0; i < trainingSetLength; i++) {
			for(int j = 0; j < outputDimensions; j++) {
				ideal.set(i * outputDimensions + j, trainingSet.getOutput(i).get(j)); 
			}
		}
		
		Vector actual = new Vector(trainingSetLength * outputDimensions);
		for(int i = 0; i < trainingSetLength; i++) {
			Vector currentOutputVector = algorithm.evaluate(trainingSet.getInput(i));
			for(int j = 0; j < outputDimensions; j++) {
				actual.set(i * outputDimensions + j, currentOutputVector.get(j));
			}
		}
		return errorCalculator.meanSquareError(ideal, actual);
	}
}
