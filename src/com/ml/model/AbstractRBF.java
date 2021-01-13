package com.ml.model;

import com.ml.framework.Vector;

public abstract class AbstractRBF extends MLAlgorithm{
	
	private int dimensions;
	private int startIndex;
	
	public AbstractRBF(Vector ltm, int dimensions, int startIndex) {
		this.ltm = ltm;
		this.startIndex = startIndex;
		this.dimensions = dimensions;
	}
	
	public Vector getCenter() {
		float[] center = new float[dimensions];
		for(int i = 0; i < center.length; i++) center[i] = ltm.get(startIndex + i);
		return new Vector(center);
	}
	
	public float getCenter(int dimension) {
		return ltm.get(startIndex + dimension);
	}
	
	public void setCenter(int dimension, float center) {
		this.ltm.set(startIndex + dimension, center);
	}
	
	public float getWidth() {
		return ltm.get(startIndex + (dimensions));
	}
	
	public void setWidth(float width) {
		ltm.set(startIndex + (dimensions), width);
	}
	
	public float getDimensions() {
		return dimensions;
	}
	
}
