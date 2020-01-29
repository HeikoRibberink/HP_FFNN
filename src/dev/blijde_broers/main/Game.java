package dev.blijde_broers.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;
import java.util.LinkedList;

import dev.blijde_broers.input.KeyManager;
import dev.blijde_broers.input.MouseManager;
import dev.blijde_broers.input.MouseWheelManager;
import dev.blijde_broers.neuralNetwork.training.Trainer;

public class Game implements Runnable {
	private Window window;
	private Thread thread;
	private LoadingScreen loadingScreen;
	private boolean running = false;
	private Trainer trainer;
	
	private static final double[] LEARNING_RATE_OPTIONS = {1, 0.5, 0.2, 0.1, 0.05, 0.02, 0.01, 0.005, 0.002, 0.001, 0.0005, 0.0002, 0.0001, 0.00005, 0.00002, 0.00001};

	@SuppressWarnings("unused")
	private int fps;

	// De huidige status van het spel.
	public static Game GAME;

	public static int TPS = 60;

	public Game() {
		start();
	}

	public static void main(String[] args) {
		GAME = new Game();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() throws InterruptedException {
		window.dispose();
		running = false;
	}

	public void init() {
		window = new Window("New Game", (int) (1280 * 0.9), (int) (720 * 0.9));
		loadingScreen = new LoadingScreen();
		loadingScreen.start();
		loadingScreen.percentageDone = 0;
		window.getCanvas().addKeyListener(new KeyManager());
		window.getCanvas().addMouseListener(new MouseManager());
		window.getCanvas().addMouseWheelListener(new MouseWheelManager());

		loadingScreen.percentageDone = 33;
		trainer = new Trainer();
		loadingScreen.percentageDone = 67;

		setup();

		loadingScreen.percentageDone = 100;
		try {
			loadingScreen.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setup() {
	}
	
	private boolean readyForChangeKey = true;
	private int currentLearningRateIndex = 8;

	public void tick() {
		toggleStates();
		if (readyForChangeKey) {
			if (KeyManager.pressed[KeyEvent.VK_7]) {
				currentLearningRateIndex = Math.max(0, currentLearningRateIndex - 1);
				trainer.network.learningRate = LEARNING_RATE_OPTIONS[currentLearningRateIndex];
				readyForChangeKey = false;
			}
			if (KeyManager.pressed[KeyEvent.VK_8]) {
				currentLearningRateIndex = Math.min(LEARNING_RATE_OPTIONS.length - 1, currentLearningRateIndex + 1);
				trainer.network.learningRate = LEARNING_RATE_OPTIONS[currentLearningRateIndex];
				readyForChangeKey = false;
			}
		}
		if (!KeyManager.pressed[KeyEvent.VK_7] && !KeyManager.pressed[KeyEvent.VK_8]) readyForChangeKey = true;
		if(!KeyManager.pressed[KeyEvent.VK_SPACE]) {
			if(trainer.mnistReader.currentIndex > trainer.currentTrainingIndex) trainer.trainNext();
		}
		if(trainer.currentTrainingIndex > 55000) {
			trainer.currentTrainingIndex = 0;
		}
	}

	public void render() {
		BufferStrategy bs = window.getCanvas().getBufferStrategy();
		if (bs == null) {
			window.getCanvas().createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		g.setColor(Color.white);

		g.setColor(Color.white);
		g.drawString(Integer.toString(TPS), 10, 40);
		if (trainer.mnistReader.data[trainer.currentTrainingIndex] != null) {
			trainer.mnistReader.data[trainer.currentTrainingIndex].display(g, 100, 100, 300, 300);
			g.setColor(Color.white);
//			g.drawString(Integer.toString(trainer.mnistReader.data[trainer.currentTrainingIndex].correctAnswer), 700, 100);
			g.drawString(Double.toString(trainer.averageError), 50, 40);
			g.drawString(Integer.toString(trainer.currentTrainingIndex), 200, 40);
			DecimalFormat df = new DecimalFormat("#.#####");
			g.drawString(df.format(LEARNING_RATE_OPTIONS[currentLearningRateIndex]), 300, 40);
			
		}
		g.setColor(Color.white);
		if (trainer.out != null) {
			for (int i = 0; i < trainer.out.length; i++) {
				g.drawString(Double.toString(Math.round(trainer.out[i] * 100)), 500, 100 + (i * 50));
			}
			double[] out = trainer.out;
			int highestValueID = 0;
			for(int i = 0; i < 10; i++) {
				if(out[i] > out[highestValueID]) {
					highestValueID = i;
				}
			}
			g.drawString(highestValueID + "", 700, 100);
		}

		g.dispose();
		bs.show();
	}

	public void run() {
		init();
		int frames = 0;
		long lastTime = System.nanoTime();
		double ns = 1000000000 / TPS;
		double delta = 0;
		long timer = System.currentTimeMillis();

		LinkedList<Long> renderTime = new LinkedList<Long>();
		LinkedList<Long> tickTime = new LinkedList<Long>();
		while (running) {
			ns = 1000000000 / TPS;
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				long start = System.nanoTime();
				tick();
				tickTime.add((System.nanoTime() - start));
				delta--;
			}
			if (running) {
				frames++;
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				fps = frames;
				frames = 0;
				long average = 0;
				for (long l : tickTime) {
					average += l;
				}
				if (tickTime.size() > 0)
					System.out.println("Tick: " + (average / tickTime.size()) / 1000);
				average = 0;
				for (long l : renderTime) {
					average += l;
				}
				if (renderTime.size() > 0)
					System.out.println("Render: " + (average / renderTime.size()) / 1000);
				while (tickTime.size() > 0) {
					tickTime.removeFirst();
				}
				while (renderTime.size() > 0) {
					renderTime.removeFirst();
				}
			}
			if (running) {
				long start = System.nanoTime();
				render();
				renderTime.add((System.nanoTime() - start));
			}
		}
	}

	public void toggleStates() {
		if (KeyManager.pressed[KeyEvent.VK_0])
			TPS++;
		if (KeyManager.pressed[KeyEvent.VK_9])
			TPS--;
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
