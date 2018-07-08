package com.jaybaffoni;

import java.awt.image.BufferedImage;

public class ItemHolder {
	
	int id;
	int quantity;
	BufferedImage image;
	public ItemHolder(int id, int quantity, BufferedImage image) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.image = image;
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
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	

}
