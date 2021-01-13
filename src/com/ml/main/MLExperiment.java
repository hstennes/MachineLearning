package com.ml.main;

import java.io.IOException;
import java.util.Random;

import com.ml.framework.DataReader;
import com.ml.framework.DataWriter;
import com.ml.framework.Trainer;
import com.ml.framework.TrainingSet;
import com.ml.framework.Vector;
import com.ml.model.MLAlgorithm;
import com.ml.model.Polynomial;
import com.ml.model.RBFNetwork;

public class MLExperiment implements Runnable{
	
	private Thread thread;
	private Polynomial p;
	private RBFNetwork r;
	private MLAlgorithm algorithm;
	private Trainer trainer;
	private DataReader dataReader;
	private DataWriter dataWriter;
	
	public MLExperiment() {
		p = new Polynomial(2, new Vector(new float[] {2, 4, 7}));
		r = new RBFNetwork(4, 3, 5);
		trainer = new Trainer(this);
		dataReader = new DataReader();
		dataWriter = new DataWriter();
		start();
		System.out.println("Application started");
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		try{
			thread.join();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		hillClimbIris();
		stop();
	}
	
	public void annealIris() {
		algorithm = r;
		hillClimbIris();
        r.getLtm().print();
	}
	
	public void hillClimbIris() {
		algorithm = r;    
		trainer.createIrisTrainingSet();
		Vector ltm = trainer.trainHillClimbing(0.01f, 0.001f);
		System.out.println("LTM loaded.  Final long term memory: ");
		ltm.print();
		try {
			dataWriter.saveLtm("data/ltm_2.txt", ltm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		algorithm.setLtm(ltm);
		System.out.println("Should be virginica");
		algorithm.evaluate(new Vector(new float[] {6.9f, 3.1f, 5.1f, 2.3f})).print(); //virginica
		System.out.println("Should be versicolor");
		algorithm.evaluate(new Vector(new float[] {5.0f, 2.3f, 3.3f, 1.0f})).print(); //versicolor
		System.out.println("Should be setosa");
		algorithm.evaluate(new Vector(new float[] {5.0f, 3.3f, 1.4f, 0.2f})).print(); //setosa
		System.out.println("Should be virginica");
		algorithm.evaluate(new Vector(new float[] {6.0f, 3.0f, 4.8f, 1.8f})).print(); //virginica
		System.out.println("Should be versicolor");
		algorithm.evaluate(new Vector(new float[] {6.0f, 3.4f, 4.5f, 1.6f})).print(); //versicolor
		System.out.println("Should be setosa");
		algorithm.evaluate(new Vector(new float[] {4.5f, 2.3f, 1.3f, 0.3f})).print(); //setosa
	}
	 
	public void testDataReader() {
		TrainingSet t = null;
		try {
			t = dataReader.readDataSet("data/IrisDataSet.txt", 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < t.getInputArray().length; i++) {
			t.getInput(i).print();
			t.getOutput(i).print();
		}
	}
	
	public void testXOR() {
		//the rbf network must be set to 2 inputs and one output before this method is called
		trainer.createXORTrainingSet();
		Vector ltm = trainer.trainHillClimbing(0.01f, 0.001f);
		algorithm.setLtm(ltm);
		algorithm.evaluate(new Vector(new float[] {0, 0})).print();
		algorithm.evaluate(new Vector(new float[] {0, 1})).print();
		algorithm.evaluate(new Vector(new float[] {1, 0})).print();
		algorithm.evaluate(new Vector(new float[] {1, 1})).print();
	}
	
	public void trainPolynomial() {
		algorithm = p;
		trainer.createPolyTrainingSet(-100, 200);
		Vector ltm = trainer.trainHillClimbing(0.01f, 0.001f); 
		ltm.print();
		System.out.println("Score: " + trainer.calculateScore(ltm));
	}
	
	public void testRBFNetwork() {
		RBFNetwork joe = new RBFNetwork(4, 3, 3);
		for(int i = 0; i < joe.getLtm().getLength(); i++) joe.setLtm(i, new Random().nextInt(100));
		System.out.println("Randomized long term memory: ");
		joe.getLtm().print();
		System.out.println("Width of rbf #0");
		System.out.println(joe.rbf[0].getWidth());
		System.out.println("Center of rbf #0");
		joe.rbf[0].getCenter().print();
		System.out.println("Now evalutating");
		joe.evaluate(new Vector(new float[] {0, 0, 0, 0})).print();
	}
	
	public MLAlgorithm getAlgorithm() {
		return algorithm;
	}
	
	public DataReader getDataReader() {
		return dataReader;
	}
	
	public static void main(String[] args) {
		new MLExperiment();
	}
}
