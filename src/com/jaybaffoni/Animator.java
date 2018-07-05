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
		return frames.get(currentFrame);
	}
	
	public void start() {
		running = true;
		prevTime = 0;
		frameAtPause = 0;
		currentFrame = 0;
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
