package com.ml.framework;

public class ErrorCalculator {
	
	public ErrorCalculator() {
		
	}
	
	public double meanSquareError(Vector ideal, Vector actual){
		if(ideal.getLength() == actual.getLength()) {
			double sum = 0;
			for(int i = 0; i < ideal.getLength(); i++) {
				sum += (ideal.get(i) - actual.get(i)) * (ideal.get(i) - actual.get(i));
			}
			return sum / ideal.getLength();
		}
		else {
			System.out.println("Error calculation error: Ideal and actual vectors differ in length");
			return Double.NaN;
		}
	}
}
