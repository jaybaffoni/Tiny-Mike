package com.jaybaffoni;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHolder {
	
	BufferedImage [] dr_nitrogen_right = new BufferedImage[4];
	//BufferedImage [] dr_nitrogen_down;
	//BufferedImage [] dr_nitrogen_left;
	//BufferedImage [] dr_nitrogen_up;
	
	public ImageHolder() {
		
		try {
			BufferedImage full = ImageIO.read(new File("src/images/dr_nitrogen_right_sheet.png"));
			for(int i = 0; i < 4; i++) {
				dr_nitrogen_right[i] = full.getSubimage(i*32, 0, 32, 32);
			}
		} catch (IOException e) {
			System.out.println("could not find image");
			e.printStackTrace();
		}
		
	}

}
