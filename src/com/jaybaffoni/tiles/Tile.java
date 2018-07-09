package com.jaybaffoni.tiles;

import java.util.ArrayList;

import com.jaybaffoni.Item;

public abstract class Tile {
	
	int id;
	ArrayList<Item> drops = new ArrayList<Item>();
	long restoreTime;
	long prevTime = System.currentTimeMillis();
	
	public Tile(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public Item getDrop() {
		return drops.get(0);
	}
	
	public ArrayList<Item> getDrops(){
		return drops;
	}
	
	public void addDrops(ArrayList<Item> toAdd) {
		drops.addAll(toAdd);
	}
	
	public void takeItem() {
		prevTime = System.currentTimeMillis();
		drops.remove(0);
	}
	
	public abstract void update();
	
	public abstract void harvest();

}
