package dev.blijde_broers.neuralNetwork;

public class Neuron {
	
	protected Synapse[] in, out;
	protected double bias;
	protected double derivative;
	protected double activation;
	protected double expectedValue;

	public Neuron(Synapse[] in, double bias, Synapse[] out) {
		this.in = in;
		this.bias = bias;
		this.out = out;
	}
	
	public Neuron(double bias) {
		// Hallo heiko
		this.bias = bias;
	}
	
	public Neuron() {
		
	}
	
	public double weightedSumWithActivation() {
		double weightedSum = 0;
		for(Synapse in : in) {
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
