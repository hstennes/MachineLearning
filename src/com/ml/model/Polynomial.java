package com.ml.model;

import com.ml.framework.Vector;;

public class Polynomial extends MLAlgorithm{

	private int degree;
	
	public Polynomial(int degree, Vector coeffs) {
		this.degree = degree;
		this.ltm = coeffs;
	}
	
	@Override
	public Vector evaluate(Vector input) {
		boolean valid = verify();
		float sum = 0;
		if(valid && input.getLength() == 1) {
			for(int i = degree; i >= 0; i--) { 
				sum += ltm.get(degree - i) * Math.pow(input.get(0), i);
			}
			return new Vector(new float[] {sum});
		}
		else {
			System.out.println("Polynomial error");
			return null;
		}
	}
	
	public boolean setDegree(int degree) {
		this.degree = degree;
		return verify();
	}
	
	public boolean setCoeffs(Vector coeffs) {
		this.ltm = coeffs;
		return verify();
	}
	
	private boolean verify() {
		return degree + 1 == ltm.getLength();
	}	
}
