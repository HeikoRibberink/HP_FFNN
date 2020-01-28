package dev.blijde_broers.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

public class MouseWheelManager extends MouseAdapter {
	public static int mouseWheelRotation = 0;
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelRotation += e.getWheelRotation();
	}

}
