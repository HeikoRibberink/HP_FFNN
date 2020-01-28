package dev.blijde_broers.neuralNetwork;

public class Synapse {
	
	protected Neuron in, out;
	protected double weight;
	protected double derivative;

	public Synapse(Neuron in, double weight, Neuron out) {
		this.in = in;
		this.weight = weight;
		this.out = out;
	}
	
	public Synapse() {
		
	}
	
	public double getWeightedOut() {
		return in.activation * weight;
	}

}
