package com.jaybaffoni.tiles;

import com.jaybaffoni.Item;
import com.jaybaffoni.Utility;

public class LavaTile extends Tile{

	public LavaTile(int id) {
		super(id);
		if(Utility.probability(2)) {
			drops.add(new Item(0));
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void harvest() {
		// TODO Auto-generated method stub
		
	}

}
