package dev.blijde_broers.neuralNetwork;

import java.io.Serializable;

public class Neuron implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Synapse[] in, out;
	protected double bias;
	protected double[] derivatives;
	protected double activation;
	protected double expectedValue;

	public Neuron(Synapse[] in, double bias, Synapse[] out) {
		this.in = in;
		this.bias = bias;
		this.out = out;
	}

	public Neuron(double bias) {
		this.bias = bias;
	}

	public Neuron() {

	}

	public void applyDerivatives(double learningRate) {
		double sum = 0;
		for (int i = 0; i < derivatives.length; i++) {
			sum += derivatives[i];
			derivatives[i] = 0;
		}

		double average = sum / derivatives.length;
		bias -= average * learningRate;
	}

	public double weightedSumWithActivation() {
		double weightedSum = 0;
		for (Synapse in : in) {
			weightedSum += in.getWeightedOut();
		}
		weightedSum += bias;
		activation = sigmoid(weightedSum);
		return activation;
	}

	public static double sigmoid(double in) {
		return 1 / (1 + (Math.pow(Math.E, -in)));
	}

}
