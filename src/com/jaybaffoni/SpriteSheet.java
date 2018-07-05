package com.jaybaffoni;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	public BufferedImage spriteSheet;
	
	public SpriteSheet(BufferedImage ss) {
		this.spriteSheet = ss;
	}
	
	public BufferedImage grabSprite(int x, int y) {
		BufferedImage sprite = spriteSheet.getSubimage(x, y, 32, 32);
		return sprite;
	}

}
