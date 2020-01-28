package dev.blijde_broers.trainingData;

import java.io.DataInputStream;
import java.io.FileInputStream;

public class MNISTReader implements Runnable {

	public DataPair[] data;
	public int setSize;
	public int imageWidth, imageHeight;
	public DataInputStream imagesStream;
	public DataInputStream labelsStream;
	public int currentIndex = 0;
	private Thread thread;

	public MNISTReader(String imagesPath, String labelsPath) {
		try {
			imagesStream = new DataInputStream(new FileInputStream(imagesPath));
			labelsStream = new DataInputStream(new FileInputStream(labelsPath));
			int magicNumber = imagesStream.readInt();
			if (magicNumber != 2051) {
				System.out.println("ImageFile's first number is: " + magicNumber + ". It should be 2051");
				imagesStream.close();
				labelsStream.close();
				throw new Exception();
			}
			magicNumber = labelsStream.readInt();
			if (magicNumber != 2049) {
				System.out.println("LabelFile's first number is: " + magicNumber + ". It should be 2049");
				imagesStream.close();
				labelsStream.close();
				throw new Exception();
			}
			int imagesSize = imagesStream.readInt();
			int labelsSize = labelsStream.readInt();
			if (imagesSize != labelsSize) {
				System.out.println("Number of images (" + imagesSize + ") is not equal to the number of labels ("
						+ labelsSize + ").");
				imagesStream.close();
				labelsStream.close();
				throw new Exception();
			}
			setSize = imagesSize;
			imageWidth = imagesStream.readInt();
			imageHeight = imagesStream.readInt();
			data = new DataPair[setSize];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void readNext(int number) {
		int tempIndex = currentIndex + number;
		try {
			for (int i = currentIndex; i < setSize && i < tempIndex; i++) {
				int[][] image = new int[imageWidth][imageHeight];
				for (int y = 0; y < imageHeight; y++) {
					for (int x = 0; x < imageWidth; x++) {
						image[x][y] = imagesStream.read();
					}
				}
				data[i] = new DataPair(image, labelsStream.read());
				currentIndex++;
				if (currentIndex % 100 == 0)
					System.out.println("DataBase.size = " + currentIndex);
			}
			if(currentIndex >= setSize) {
				imagesStream.close();
				labelsStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void readAll() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		readNext(setSize);
	}
	
	

}
