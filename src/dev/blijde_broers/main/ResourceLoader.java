package dev.blijde_broers.main;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ResourceLoader {
	public static Image loadTexture(String name) throws IOException {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(name);
			Image logo = ImageIO.read(input);
			return logo;
		} catch (Exception e) {
			File imgFile = new File(name);
			Image img;
			img = ImageIO.read(imgFile);
			return img;
		}
	}
}
