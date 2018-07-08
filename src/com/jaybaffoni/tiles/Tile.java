package com.jaybaffoni.tiles;

import java.util.concurrent.ThreadLocalRandom;

import com.jaybaffoni.Item;

public abstract class Tile {
	
	int id;
	Item drop;
	long restoreTime;
	long prevTime = System.currentTimeMillis();
	
	public Tile(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public Item getDrop() {
		return drop;
	}
	
	public void takeItem() {
		prevTime = System.currentTimeMillis();
		drop = null;
	}
	
	public abstract void update();

}
