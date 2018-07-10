package com.jaybaffoni.entities;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaybaffoni.Inventory;
import com.jaybaffoni.Item;
import com.jaybaffoni.Player;

public class Chest extends Entity {
	
	public int originalCapacity = 100;
	public int currentAvailability = 100;
	HashMap<Integer, Integer> storage = new HashMap<Integer, Integer>();
	
	public Chest() {
		this.id = 8;
		this.health = Double.POSITIVE_INFINITY;
		addToStorage(new Item(0));
	}
	
	public boolean addToStorage(Item temp) {
		if(currentAvailability - temp.getWeight() < 0) {
			return false;
		} else {
			if(storage.containsKey(temp.getId())) {
				storage.put(temp.getId(), storage.get(temp.getId()) + 1);
			} else {
				storage.put(temp.getId(), 1);
			}
			currentAvailability -= temp.getWeight();
			System.out.println(currentAvailability);
			return true;
		}
	}
	
	public boolean reduceStorage(Item temp) {
		storage.put(temp.getId(), storage.get(temp.getId()) - 1);
		currentAvailability += temp.getWeight();
		System.out.println(currentAvailability);
		if(storage.get(temp.getId()) <= 0) {
			storage.remove(temp.getId());
			return true;
		}
		return false;
	}
	
	public ArrayList<Inventory> getStorage(){
		ArrayList<Inventory> inventory = new ArrayList<Inventory>();
		for(Integer i: storage.keySet()) {
			inventory.add(new Inventory(i, storage.get(i)));
		}
		return inventory;
	}

	@Override
	public void getDefaultAction(Player player) {
		player.interact(this);
		
	}

	@Override
	public ArrayList<Item> getDrops() {
		// TODO Auto-generated method stub
		return null;
	}

}
