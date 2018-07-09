package com.jaybaffoni;

public class Inventory {
	
	int id;
	int quantity;
	String[] names = {"Lava Stone", "Hay", "Desert Flower", "Wood"};
	
	public Inventory(int id, int quantity) {
		super();
		this.id = id;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getName() {
		return names[id];
	}

}
