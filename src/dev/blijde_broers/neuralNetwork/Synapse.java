package dev.blijde_broers.neuralNetwork;

import dev.blijde_broers.neuralNetwork.training.Trainer;

public class Synapse {

	protected Neuron in, out;
	protected double weight;
	protected double[] derivatives = new double[Trainer.blockSize];

	public Synapse(Neuron in, double weight, Neuron out) {
		this.in = in;
		this.weight = weight;
		this.out = out;
	}

	public Synapse() {

	}

	public void applyDerivatives(double learningRate) {
		double sum = 0;
		for (int i = 0; i < derivatives.length; i++) {
			sum += derivatives[i];
			derivatives[i] = 0;
		}
		double average = sum / derivatives.length;
		weight -= average * learningRate;
	}

	public double getWeightedOut() {
		return in.activation * weight;
	}

}
