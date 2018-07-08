package com.jaybaffoni.tiles;

import java.util.concurrent.ThreadLocalRandom;

import com.jaybaffoni.Item;
import com.jaybaffoni.Utility;

public class GrassTile extends Tile{

	public GrassTile(int id) {
		super(id);
		restoreTime = ThreadLocalRandom.current().nextLong(30000,300000);
		if(Utility.probability(10)) {
			this.drop = new Item(1);
		}
		
	}
	
	public void update() {
		if(drop == null) {
			if(Utility.probability(.01)) {
				this.drop = new Item(1);
			}

		}
	}

}
