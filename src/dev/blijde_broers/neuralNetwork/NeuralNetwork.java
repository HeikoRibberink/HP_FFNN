package dev.blijde_broers.neuralNetwork;

public interface NeuralNetwork {
	public abstract double[] calculate(double[] in);
	public abstract void train(double[] correctAnswer);
}
