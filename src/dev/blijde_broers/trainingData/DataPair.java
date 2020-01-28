package dev.blijde_broers.trainingData;

import java.awt.Color;
import java.awt.Graphics;

public class DataPair {
	
	public int[][] image;
	public double[] data;
	public int correctAnswer;

	public DataPair(int[][] image, int num) {
		this.image = image;
		this.correctAnswer = num;
		data = new double[image.length * image[0].length];
		int index = 0;
		for(int[] i : image) {
			for(int o : i) {
				data[index] = (double) o / 255d;
				index++;
			}
		}
	}
	
	public void display(Graphics g, int x, int y, int width, int height) {
		for(int i = 0; i < image.length; i++) {
			for(int o = 0; o < image[i].length; o++) {
				g.setColor(new Color(image[o][i], image[o][i], image[o][i]));
				g.fillRect(x + (int) (o * (float) width / 28), y + (int) (i * (float) height / 28), width / 28, height / 28);
			}
		}
	}

}
