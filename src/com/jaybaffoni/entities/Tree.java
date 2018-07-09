package com.jaybaffoni.entities;

import java.util.ArrayList;

import com.jaybaffoni.Item;
import com.jaybaffoni.Player;
import com.jaybaffoni.Utility;

public class Tree extends Entity{
	//tree, apple, palm, pine
	int[] woodProb = {90,70,50,50};
	
	public Tree(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.health = 15;
	}

	@Override
	public void getDefaultAction(Player player) {
		player.destroy(this);
		
	}

	@Override
	public ArrayList<Item> getDrops() {
		ArrayList<Item> drops = new ArrayList<Item>();
		while(Utility.probability(woodProb[id])) {
			drops.add(new Item(3));
		}

		
		return drops;
	}
	
	

}
