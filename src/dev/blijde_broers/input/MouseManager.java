package dev.blijde_broers.input;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dev.blijde_broers.main.Game;

public class MouseManager extends MouseAdapter {
	public static boolean mouseDown = false;

	public void mousePressed(MouseEvent e) {
		mouseDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	public static Point getMousePosition() {
		return new Point(
				MouseInfo.getPointerInfo().getLocation().x
						- Game.GAME.getWindow().getComponents()[0].getLocationOnScreen().x,
				MouseInfo.getPointerInfo().getLocation().y
						- Game.GAME.getWindow().getComponents()[0].getLocationOnScreen().y);
	}
	
}
