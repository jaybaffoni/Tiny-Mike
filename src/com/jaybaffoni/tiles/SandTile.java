package com.jaybaffoni.tiles;

import java.util.concurrent.ThreadLocalRandom;

import com.jaybaffoni.Item;
import com.jaybaffoni.Utility;

public class SandTile extends Tile{
	
	long timeUntilDeath;
	long timeUntilGrowth;
	boolean hasFlower = false;

	public SandTile(int id) {
		super(id);
		
		if(Utility.probability(1)) {
			this.drop = new Item(2);
			hasFlower = true;
		}
	}

	@Override
	public void update() {
		if(hasFlower && drop == null) {
			if(Utility.probability(.001)) {
				this.drop = new Item(2);
			}

		}
	}

}
