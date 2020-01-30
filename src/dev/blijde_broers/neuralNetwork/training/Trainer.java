package dev.blijde_broers.neuralNetwork.training;

import dev.blijde_broers.neuralNetwork.FeedForwardNN;
import dev.blijde_broers.trainingData.MNISTReader;

public class Trainer {

	public FeedForwardNN network = new FeedForwardNN(1000, 28 * 28, 16, 16, 10);
	public MNISTReader mnistReader;
	public int currentTrainingIndex = 0;
	public double[] out;
	public double error;

	public double[] errors = new double[network.blockSize];
	public int errorIndex = 0;
	public double averageError = 0;
	
	public double[] correctAnswers = new double[10];

	public Trainer() {
		mnistReader = new MNISTReader("src/res/datasets/train-images.idx3-ubyte",
				"src/res/datasets/train-labels.idx1-ubyte");
		mnistReader.readAll();
	}

	public void testNext() {

	}

	public void trainNext() {
		currentTrainingIndex++;
		this.out = network.calculate(mnistReader.data[currentTrainingIndex].data);
		double[] correctAnswer = new double[10];
		if (out.length != correctAnswer.length)
			throw new Error("Length of network output is not equal to training data set output length.");
		for (int i = 0; i < correctAnswer.length; i++) {
			correctAnswer[i] = 0;
		}
		correctAnswer[mnistReader.data[currentTrainingIndex].correctAnswer] = 1;
		this.correctAnswers = correctAnswer;
		double error = 0;
		for (int i = 0; i < out.length; i++) {
			error += Math.pow(out[i] - correctAnswer[i], 2);
		}
		// error /= out.length;
		this.error = error;
		errors[errorIndex] = error;
		errorIndex++;
		if (errorIndex == errors.length)
			errorIndex = 0;
		averageError();
		
		System.out.println();
		System.out.println(mnistReader.data[currentTrainingIndex].correctAnswer);
		for (double d : correctAnswer) System.out.print(Math.round(d * 100) + "; ");
		System.out.println();
		for (double d : out) System.out.print(Math.round(d * 100) + "; ");
		
		network.train(correctAnswer);
		// network.train(error);
	}
	
	private void averageError() {
		double sum = 0;
		for (double d : errors) sum += d;
		averageError = sum / errors.length;
	}
}
