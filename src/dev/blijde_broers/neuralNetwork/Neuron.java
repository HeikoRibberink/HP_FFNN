package dev.blijde_broers.neuralNetwork;

import java.util.ArrayList;

public class Neuron {

	protected Synapse[] in, out;
	protected double bias;
	protected ArrayList<Double> derivatives = new ArrayList<Double>();
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
		if (derivatives.size() == 0)
			return;
		double sum = 0;
		for (double d : derivatives)
			sum += d;

		double average = sum / derivatives.size();
		bias -= average * learningRate;
		derivatives.clear();
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
