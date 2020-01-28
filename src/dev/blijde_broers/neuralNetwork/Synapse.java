package dev.blijde_broers.neuralNetwork;

import java.util.ArrayList;

public class Synapse {

	protected Neuron in, out;
	protected double weight;
	protected ArrayList<Double> derivatives = new ArrayList<Double>();

	public Synapse(Neuron in, double weight, Neuron out) {
		this.in = in;
		this.weight = weight;
		this.out = out;
	}

	public Synapse() {

	}

	public void applyDerivatives(double learningRate) {
		if (derivatives.size() == 0)
			return;
		double sum = 0;
		for (double d : derivatives)
			sum += d;
		double average = sum / derivatives.size();
		weight -= average * learningRate;
		derivatives.clear();
	}

	public double getWeightedOut() {
		return in.activation * weight;
	}

}
