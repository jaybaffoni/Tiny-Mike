package com.jaybaffoni.entities;

import java.util.ArrayList;

import com.jaybaffoni.Item;
import com.jaybaffoni.Player;
import com.jaybaffoni.Utility;

public class Tree extends Entity{
	//tree, apple, palm, pine
	int[] woodProb = {80,60,40,40};
	int[] nutProb = {25,0,0,10};
	int[] appleProb = {0,25,0,0};
	int[] coconutProb = {0,0,30,0};
	int[] bananaProb = {10,0,10,0};
	
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
		while(Utility.probability(nutProb[id])) {
			drops.add(new Item(4));
		}
		while(Utility.probability(coconutProb[id])) {
			drops.add(new Item(5));
		}
		while(Utility.probability(appleProb[id])) {
			drops.add(new Item(7));
		}
		while(Utility.probability(bananaProb[id])) {
			drops.add(new Item(6));
		}

		
		return drops;
	}
	
	

}
