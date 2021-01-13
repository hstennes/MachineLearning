package com.ml.framework;

public class TrainingSet {
	
	private int inputVectorDimensions;
	private int outputVectorDimensions;
	private Vector[] inputs;
	private Vector[] outputs;
	
	public TrainingSet(Vector[] inputs, Vector[] outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
		inputVectorDimensions = inputs[0].getLength();
		outputVectorDimensions = outputs[0].getLength();
	}
	
	public TrainingSet() {}
	 
	public boolean verify() {
		boolean valid = true;
		if(inputs.length == outputs.length) {
			for(int i = 0; i < inputs.length; i++) {
				if(inputs[i].getLength() != inputVectorDimensions || outputs[i].getLength() != outputVectorDimensions) {
					System.out.println("TrainingSet error: Some data points differ in size");
					valid = false;
					break;
				}
			}
		}
		else {
			System.out.println("TrainingSet error: Different number of inputs and outputs");
			valid = false;
		}
		return valid;
	}
	
	public int getInputDimensions() {
		return inputs[0].getLength();
	}
	
	public int getOutputDimensions() {
		return outputs[0].getLength();
	}
	
	public Vector[] getInputArray() {
		return inputs;
	}
	
	public Vector[] getOutputArray() {
		return outputs;
	}
	
	public void setInputArray(Vector[] inputs) {
		this.inputs = inputs;
	}
	
	public void setOutputArray(Vector[] outputs) {
		this.outputs = outputs;
	}
	
	public void setInput(int index, Vector v) {
		if(inputs != null) inputs[index] = v;
	}
	
	public void setOutput(int index, Vector v) {
		if(outputs != null) outputs[index] = v;
	}
	
	public Vector getInput(int index) {
		if(outputs != null) return inputs[index];
		else return null;
	}
	
	public Vector getOutput(int index) {
		if(outputs != null) return outputs[index];
		else return null;
	}
	
	public int getLength() {
		return outputs.length;
	}
}
