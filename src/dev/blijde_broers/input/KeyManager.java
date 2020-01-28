package dev.blijde_broers.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

	public static boolean[] pressed = new boolean[1024];

	@Override
	public void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public static void init() {
		for (int i = 0; i < pressed.length; i++) {
			pressed[i] = false;
		}
	}

}