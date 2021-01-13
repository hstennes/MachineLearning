package com.ml.model;

import com.ml.framework.Vector;

public class GaussianFunction extends AbstractRBF{

	public GaussianFunction(Vector ltm, int dimensions, int startIndex) {
		super(ltm, dimensions, startIndex);
	}

	@Override
	public Vector evaluate(Vector input) {
		double value = 0;
        final double width = getWidth();
        //System.out.println("RBF " + this + " has a width of " + width + " and a center[0] of " + getCenter().get(0));
        for (int i = 0; i < getDimensions(); i++) {
            final double center = this.getCenter(i);
            value += Math.pow(input.get(i) - center, 2) / (2.0 * width * width);
        }
        return new Vector(new float[]{(float) Math.exp(-value)});
	}

}
