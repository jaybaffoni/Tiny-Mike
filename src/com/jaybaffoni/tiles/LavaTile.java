package com.jaybaffoni.tiles;

import com.jaybaffoni.Item;
import com.jaybaffoni.Utility;

public class LavaTile extends Tile{

	public LavaTile(int id) {
		super(id);
		if(Utility.probability(1)) {
			this.drop = new Item(0);
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
