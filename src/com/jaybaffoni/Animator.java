package com.jaybaffoni;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animator {
	
	ArrayList<BufferedImage> frames;
	//BufferedImage sprite;
	boolean running = false;
	long prevTime, speed;
	int currentFrame, frameAtPause;
	
	public Animator() {
		
	}
	
	public void setFrames(ArrayList<BufferedImage> frames) {
		this.frames = frames;
	}
	
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	
	public void update(long time) {
		if(running) {
			//System.out.println(currentFrame);
			if(time - prevTime >= speed) {
				//update frame
				currentFrame++;
				if(currentFrame >= frames.size()) {
					currentFrame = 0;
				}
				//sprite = frames.get(currentFrame);
				prevTime = time;
			}
		}
	}
	
	public BufferedImage getFrame() {
		//System.out.println(currentFrame);
		return frames.get(currentFrame);
	}
	
	public void start() {
		running = true;
		prevTime = System.currentTimeMillis();
		frameAtPause = 1;
		currentFrame = 1;
	}
	
	public void stop() {
		running = false;
		prevTime = 0;
		frameAtPause = 0;
		currentFrame = 0;
	}
	
	public void pause() {
		frameAtPause = currentFrame;
		running = false;
	}
	
	public void resume() {
		currentFrame = frameAtPause;
		running = true;
	}

}
