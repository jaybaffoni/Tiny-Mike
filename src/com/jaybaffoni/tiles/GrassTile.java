package com.jaybaffoni.tiles;

import com.jaybaffoni.Item;
import com.jaybaffoni.Utility;

public class GrassTile extends Tile{

	public GrassTile(int id) {
		super(id);

	}
	
	public void harvest() {
		if(Utility.probability(25)){
			drops.add(new Item(1));
		}
	}
	
	public void update() {

	}

}
