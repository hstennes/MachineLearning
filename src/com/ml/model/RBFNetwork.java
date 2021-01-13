package com.ml.model;

import com.ml.framework.Vector;

public class RBFNetwork extends MLAlgorithm{

	private int inputCount;
	private int outputCount;
	public AbstractRBF[] rbf;
	private int indexInputWeights;
	private int indexOutputWeights;
	
	public RBFNetwork(int inputCount, int outputCount, int rbfCount) {
		this.inputCount = inputCount;
		this.outputCount = outputCount;
		rbf = new AbstractRBF[rbfCount];
		
		final int inputWeightCount = inputCount * rbfCount;
		final int outputWeightCount = outputCount * (rbfCount + 1);
		final int rbfParams = (inputCount + 1) * rbfCount;
	
		ltm = new Vector(new float[inputWeightCount + outputWeightCount + rbfParams]);
		indexInputWeights = 0;
		indexOutputWeights = inputWeightCount + rbfParams;
		rbf = new AbstractRBF[rbfCount];
		
		for (int i = 0; i < rbfCount; i++) {
            final int rbfIndex = inputWeightCount + ((inputCount + 1) * i);
            this.rbf[i] = new GaussianFunction(ltm, inputCount, rbfIndex);
        }
	}
	
	@Override
	public Vector evaluate(Vector input) {
		float[] rbfOutput = new float[rbf.length + 1];
		rbfOutput[rbfOutput.length - 1] = 1;
		for(int rbfIndex = 0; rbfIndex < rbf.length; rbfIndex++) {
			float[] weightedInput = new float[input.getLength()];
			for(int inputIndex = 0; inputIndex < input.getLength(); inputIndex++) {
				int memoryIndex = indexInputWeights + (rbfIndex * inputCount) + inputIndex;
				weightedInput[inputIndex] = input.get(inputIndex) * ltm.get(memoryIndex);
			}
			rbfOutput[rbfIndex] = rbf[rbfIndex].evaluate(new Vector(weightedInput)).get(0);
		}
		float[] result = new float[outputCount];
		for(int outputIndex = 0; outputIndex < result.length; outputIndex++) {
			float sum = 0;
			for(int rbfIndex = 0; rbfIndex < rbfOutput.length; rbfIndex++) {
				int memoryIndex = indexOutputWeights + (outputIndex * (rbf.length + 1)) + rbfIndex;
				sum += rbfOutput[rbfIndex] * ltm.get(memoryIndex);
			}
			result[outputIndex] = sum;
		}
		return new Vector(result);
	}

}