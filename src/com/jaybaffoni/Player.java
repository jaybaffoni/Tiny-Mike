package com.jaybaffoni;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
	
	int x;
	int y;
	int dir;
	int speed = 5;
	int delay = 100;
	long prevTime = 0;
	boolean moving = false;
	Animator animator;
	ArrayList<ArrayList<BufferedImage>> images = new ArrayList<ArrayList<BufferedImage>>();
	
	int[] xMove = {1,0,-1,0};
	int[] yMove = {0,1,0,-1};
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		for(int i = 0; i < 4; i++) {
			images.add(new ArrayList<BufferedImage>());
		}
		loadImages();
	}
	
	public void update() {
		//System.out.println("updating...");
		long time = System.currentTimeMillis();
		if(moving) {
			animator.update(time);
			move(dir, false);
		}
		
	}
	
	public void go(int direction, boolean strafe) {
		dir = direction;
		animator.setFrames(images.get(dir));
		if(!moving) {
			System.out.println("moving");
			animator.start();
			moving = true;
		}
	}
	
	public void move(int direction, boolean strafe) {
		long time = System.currentTimeMillis();
		if(time - prevTime >= delay) {
			x += xMove[dir] * speed;
			y += yMove[dir] * speed;
			prevTime = time;
		}
		
		
	}
	
	public void stop() {
		System.out.println("stopping");
		moving = false;
		animator.stop();
	}
	
	public void loadImages() {
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage ssRight = null, ssDown = null, ssLeft = null, ssUp = null;
		try {
			ssRight = loader.loadImage("images/dr_nitrogen_right_sheet.png");
			ssDown = loader.loadImage("images/dr_nitrogen_down_sheet.png");
			ssLeft = loader.loadImage("images/dr_nitrogen_left_sheet.png");
			ssUp = loader.loadImage("images/dr_nitrogen_up_sheet.png");
		} catch(IOException e) {
			e.printStackTrace();
		}
		SpriteSheet ssR = new SpriteSheet(ssRight);
		SpriteSheet ssD = new SpriteSheet(ssDown);
		SpriteSheet ssL = new SpriteSheet(ssLeft);
		SpriteSheet ssU = new SpriteSheet(ssUp);
		for(int i = 0; i < 4; i++) {
			images.get(0).add(ssR.grabSprite(i*32, 0));
			images.get(1).add(ssD.grabSprite(i*32, 0));
			images.get(2).add(ssL.grabSprite(i*32, 0));
			images.get(3).add(ssU.grabSprite(i*32, 0));
		}
		animator = new Animator();
		animator.setFrames(images.get(0));
		animator.setSpeed(100);
		//animator.start();
	}
	
	public BufferedImage getImage() {
		return animator.getFrame();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
