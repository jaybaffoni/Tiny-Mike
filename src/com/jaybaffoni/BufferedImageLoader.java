package com.jaybaffoni;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class BufferedImageLoader {
	
	public BufferedImage loadImage(String relativePath) throws IOException {
		URL url = this.getClass().getResource(relativePath);
		return ImageIO.read(url);
	}
	
}
