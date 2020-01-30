package dev.blijde_broers.neuralNetwork;

import java.util.ArrayList;

public class FeedForwardNN implements NeuralNetwork {

	// The amount of layers in the network structure must be more than 1
	// The first layer of the structure is just used for inputs, nothing else
	public ArrayList<ArrayList<Neuron>> structure;
	public double learningRate = .001;
	public int[] structureLayout;
	public final int blockSize;
	public int blockIndex = 0;

	public FeedForwardNN(int blockSize, int... structureLayout) {
		this.blockSize = blockSize;
		this.structureLayout = structureLayout;
		structure = new ArrayList<ArrayList<Neuron>>();

		for (int layerID = 0; layerID < structureLayout.length; layerID++) {
			structure.add(new ArrayList<Neuron>());
			for (int neuronID = 0; neuronID < structureLayout[layerID]; neuronID++) {
				Neuron neuron = new Neuron((Math.random() - 0.5) * 2);
				neuron.derivatives = new double[blockSize];
				
				if (layerID != 0)
					neuron.in = new Synapse[structureLayout[layerID - 1]];
				if (layerID != structureLayout.length - 1)
					neuron.out = new Synapse[structureLayout[layerID + 1]];
				
				structure.get(layerID).add(neuron);
			}
		}
		
		for (int layerID = 0; layerID < structureLayout.length - 1; layerID++) {
			ArrayList<Neuron> layer = structure.get(layerID);
			ArrayList<Neuron> nextLayer = structure.get(layerID + 1);
			for (int inputNeuronID = 0; inputNeuronID < layer.size(); inputNeuronID++) {
				Neuron inputNeuron = layer.get(inputNeuronID);
				for (int outputNeuronID = 0; outputNeuronID < nextLayer.size(); outputNeuronID++) {
					Neuron outputNeuron = nextLayer.get(outputNeuronID);
					Synapse synapse = new Synapse(inputNeuron, (Math.random() - 0.5) * 2, outputNeuron);
					synapse.derivatives = new double[blockSize];
					inputNeuron.out[outputNeuronID] = synapse;
					outputNeuron.in[inputNeuronID] = synapse;
				}
			}
		}
	}

//	public static void main(String[] args) {
//		FeedForwardNN nn = new FeedForwardNN(4, 5, 4);
//		double[] inputs = {1,0,1,0};
//		double[] output = {0.2,0.8,0.8,0.2};
//		for (int i = 0; i < 100000000; i++) {
//			double[] out = nn.calculate(inputs);
//			nn.train(output);
//			if (i % 1000000 == 0) {
//				System.out.println("_______");
//				for (double d : out) {
//					System.out.println(d);
//				} 
//			}
//		}
//	}

	@Override
	public double[] calculate(double[] in) {
		// Set the output of the first layer of neurons to the input
		for (int i = 0; i < structure.get(0).size(); i++) {
			structure.get(0).get(i).activation = in[i];
		}
		for (int layer = 1; layer < structure.size(); layer++) {
			for (int neuron = 0; neuron < structure.get(layer).size(); neuron++) {
				structure.get(layer).get(neuron).weightedSumWithActivation();
			}
		}
		double[] out = new double[structure.get(structure.size() - 1).size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = structure.get(structure.size() - 1).get(i).activation;
		}
		return out;
	}

	@Override
	public void train(double[] correctAnswers) {
		// Set expected value of output neurons
		for (int neuronIndex = 0; neuronIndex < structureLayout[structureLayout.length - 1]; neuronIndex++) {
			structure.get(structureLayout.length - 1).get(neuronIndex).expectedValue = correctAnswers[neuronIndex];
		}

		// Calculate derivatives
		for (int layerID = structure.size() - 1; layerID > 0; layerID--) {
			if (layerID > 1) {
				for (Neuron neuron : structure.get(layerID - 1)) {
					neuron.expectedValue = neuron.activation;
				}
			}
			ArrayList<Neuron> layer = structure.get(layerID);
			for (int neuronID = 0; neuronID < layer.size(); neuronID++) {
				Neuron neuron = layer.get(neuronID);

				double aL0 = neuron.activation;
				double y = neuron.expectedValue;
				// Loop over synapses
				for (Synapse synapse : neuron.in) {
					// Calculate weight derivative
					Neuron inputNeuron = synapse.in;
					double aL1 = inputNeuron.activation;
					synapse.derivatives[blockIndex] = (2 * aL0 * aL1 * (aL0 - y) * (1 - aL0));

					// Change expected value of previous layer
					if (layerID > 1) {
						double w = synapse.weight;
						inputNeuron.expectedValue -= 2 * w * aL0 * (1 - aL0) * (aL0 - y) * learningRate;
					}
				}
				// Calculate bias derivative
				neuron.derivatives[blockIndex] = (2 * aL0 * (aL0 - y) * (1 - aL0));
			}
		}
		
		blockIndex++;
		if (blockIndex == blockSize) {
			applyLearning();
			blockIndex = 0;
		}
	}
	
	public void applyLearning() {
		for (ArrayList<Neuron> layer : structure) {
			for (Neuron neuron : layer) {
				neuron.applyDerivatives(learningRate);
			}
		}
		for (int i = 0; i < structure.size() - 1; i++) {
			for (Neuron neuron : structure.get(i)) {
				for (Synapse synapse : neuron.out) {
					synapse.applyDerivatives(learningRate);
				}
			}
		}
	}

	public static double sigmoid(double in) {
		return 1 / (1 + (Math.pow(Math.E, -in)));
	}

}
