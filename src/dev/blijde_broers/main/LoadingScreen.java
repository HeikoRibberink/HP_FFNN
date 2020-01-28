package dev.blijde_broers.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class LoadingScreen implements Runnable {

	private boolean running = false;
	private Thread thread;

	private String loading;
	public int percentageDone;
	private int timeOut;

	public LoadingScreen() {
		
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		System.out.println("Started loading");
	}

	public synchronized void stop() throws InterruptedException {
		running = false;
		thread.join();
		System.out.println("Stopped loading");
	}

	public void tick() {
		if (timeOut > 10) {
			if (loading.length() < 11) {
				loading += ".";
			} else {
				loading = "Loading";
			}
			timeOut = 0;
		}
		timeOut++;
	}

	public void render() {
		BufferStrategy bs = Game.GAME.getWindow().getCanvas().getBufferStrategy();
		if (bs == null) {
			Game.GAME.getWindow().getCanvas().createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, 1280, 720);
		g.setColor(Color.white);

		g.drawRect(10, 10, 100, 20);
		g.fillRect(10, 10, percentageDone, 20);
		g.setColor(Color.gray);
		g.drawString(loading, 20, 25);

		g.dispose();
		bs.show();
	}
	
	public void init() {
		loading = "Loading";
		percentageDone = 0;
		timeOut = 0;
	}

	@Override
	public void run() {
		init();
		int frames = 0;
		long lastTime = System.nanoTime();
		double ns = 1000000000 / Game.TPS;
		double delta = 0;
		long timer = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running) {
				frames++;
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
			if (running) {
				render();
			}
		}

	}

}
