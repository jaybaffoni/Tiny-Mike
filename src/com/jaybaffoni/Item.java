package com.jaybaffoni;

public class Item {
	
int id;
int[] weights = {2,1,1,4,1,2,2,1};
//boolean[] visible = {true, false, true};

	
	public Item(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWeight() {
		return weights[id];
	}

	/*public boolean isVisible() {
		return visible[id];
	}*/
	
}
