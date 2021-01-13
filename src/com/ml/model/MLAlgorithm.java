package com.ml.model;

import com.ml.framework.Vector;

public abstract class MLAlgorithm {
	
	protected Vector ltm;
	
	public abstract Vector evaluate(Vector input);

	public Vector getLtm() { 
		return ltm;
	}
	
	public void setLtm(Vector ltm) {
		for(int i = 0; i < this.ltm.getLength(); i++) {
			this.ltm.set(i, ltm.get(i));
		}
	}
	
	public void setLtm(int index, float value) {
		ltm.set(index, value);
	}
}
