package com.ml.framework;

public class Vector implements Cloneable{
	
	private float[] vector;
	
	public Vector(int dimensions) {
		vector = new float[dimensions]; 
		for(int i = 0; i < vector.length; i++) vector[i] = 0;
	}
	
	public Vector(float[] vector) {
		this.vector = vector;
	}
	
	public float get(int i) {
		return vector[i]; 
	}
	
	public float[] getFloatArray() {
		return vector;
	}
	
	public double[] getDoubleArray() {
		double[] doubleVector = new double[vector.length];
		for(int i = 0; i < vector.length; i++) doubleVector[i] = vector[i];
		return doubleVector;
	}
	
	public void set(int i, float x) {
		vector[i] = x;
	}
	
	public void set(float[] vector) {
		this.vector = vector;
	}
	
	public float EuclideanDist(Vector v) {
		if(isComparable(v)) {
			int sum = 0;
			for(int i = 0; i < vector.length; i++) {
				sum += (vector[i] - v.get(i)) * (vector[i] - v.get(i));
			}
			return (float) Math.sqrt(sum);
		}
		else {
			System.out.println("Vector error: Vectors in distance calculation differ in length");
			return Float.NaN;
		}
	}
	
	public void changeLength(int newLength) {
		float[] temp = vector.clone();
		vector = new float[newLength];
		for(int i = 0; i < Math.min(temp.length, vector.length); i++) { 
			vector[i] = temp[i];
		}
	}
	
	public int getLength() {
		return vector.length;
	}
	
	public void print() {
		System.out.println("Values in vector: " + this);
		for(int i = 0; i < vector.length; i++) {
			System.out.println("Index " + i + " = " + vector[i]);
		}
	}
	
	public boolean isComparable(Vector v) {
		return getLength() == v.getLength();
	}
	
	@Override 
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
