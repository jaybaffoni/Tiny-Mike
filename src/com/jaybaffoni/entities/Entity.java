package com.jaybaffoni.entities;

import java.util.ArrayList;

import com.jaybaffoni.Item;
import com.jaybaffoni.Player;

public abstract class Entity {
	
	/*
	 * 0 - Tree
	 * 1 - Apple Tree
	 * 2 - Palm Tree
	 * 3 - 
	 * 4 -
	 * 5 -
	 * 6 -
	 * 7 -
	 * 8 - Chest 
	 */
	
	int id;
	double health;
	int x, y;
	
	public boolean reduceHealth(int attack) {
		health -= attack;
		return health <= 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	
	public abstract void getDefaultAction(Player player);
	
	public abstract ArrayList<Item> getDrops();
	

}
