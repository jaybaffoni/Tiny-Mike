package com.jaybaffoni;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.jaybaffoni.entities.Chest;
import com.jaybaffoni.entities.Entity;
import com.jaybaffoni.tiles.Tile;

public class Player extends Entity{
	//player version 7/7/18
	String name;
	int tileX;
	int tileY;
	int x;
	int y;
	int relativeX;
	int relativeY;
	int newX, newY;
	int dir;
	int speed = 8;
	int delayTime = 10;
	long prevTime = 0;
	boolean moving = false;
	Animator animator;
	ArrayList<ArrayList<BufferedImage>> images = new ArrayList<ArrayList<BufferedImage>>();
	boolean lead = true;
	Player tail = null;
	boolean swapping = false;
	Tile[][] theMap;
	Entity[][] theObjects;
	
	int[] xMove = {1,0,-1,0};
	int[] yMove = {0,1,0,-1};
	
	int totalInventory = 25;
	int availableInventory = totalInventory;
	int[] quantities = new int[100];
	boolean isAcquiring = false;
	long acquireTime = 0;
	long prevAcquireTime = 0L;
	Item recentDrop = null;
	
	int attack = 1;
	int destroy = 1;
	int equip = -1;
	
	Chest currentStorage = null;
	
	public Player(int x, int y, boolean isLead, String name, Tile[][] theMap, Entity[][] objects) {
		this.theMap = theMap;
		this.theObjects = objects;
		this.name = name;
		this.lead = isLead;
		if(lead) {
			relativeX = 4;
			relativeY = 4;
		} else {
			relativeX = 3;
			relativeY = 4;
		}
		
		tileX = x;
		tileY = y;
		this.x = this.newX = relativeX*64;
		this.y = this.newY = relativeY*64;
		for(int i = 0; i < 4; i++) {
			images.add(new ArrayList<BufferedImage>());
		}
		loadImages();
		for(int i = 0; i < 100; i++) {
			quantities[i] = 0;
		}
	}
	
	public void update() {
		//System.out.println("upd20ating...");
		long time = System.currentTimeMillis();
		
		if(isAcquiring) {
			//System.out.println("current time: " + time);
			//System.out.println("prev time:    " + prevAcquireTime);
			long diff2 = time - prevAcquireTime;
			//System.out.println(diff2);
			//System.out.println(time + " - " + prevAcquireTime + " = " + diff2);
			//System.out.println("acquiring");
			acquireTime -= diff2;
			prevAcquireTime = time;
			//System.out.println(acquireTime);
			if(acquireTime <= 0) {
				isAcquiring = false;
			}
		}
		
		if(moving) {
			
			animator.update(time);
			if(x != newX || y != newY) {
				move(dir, false);
			} else {
				stop();
			}
			
		}
		if(tail != null) {
			tail.update();
		}
	}
	
	public void action() {
		
		Entity temp = theObjects[tileX + xMove[dir]][tileY + yMove[dir]];
		if(temp != null) {
			temp.getDefaultAction(this);
		} else {
			theMap[tileX + xMove[dir]][tileY + yMove[dir]].harvest();
		}
		
	}
	
	public void attack() {
		
	}
	
	public void interact(Entity temp) {
		if(temp instanceof Chest) {
			currentStorage = (Chest) temp;
			tail.currentStorage = (Chest) temp;
		}
	}
	
	public void destroy(Entity temp) {
		if(temp.reduceHealth(destroy)) {
			theMap[tileX + xMove[dir]][tileY + yMove[dir]].addDrops(temp.getDrops());
			theObjects[tileX + xMove[dir]][tileY + yMove[dir]] = null;
			
		}
	}
	
	public void harvest() {
		if(!isAcquiring) {
			Iterator<Item> iterator = theMap[tileX][tileY].getDrops().iterator();
			while (iterator.hasNext()) {
			   Item drop = iterator.next();
			   if(addItemToInventory(drop)) {
				   iterator.remove();
			   }
			}
		}
		
	}
	
	public boolean isAcquiring() {
		return isAcquiring;
	}
	
	public boolean addItemToInventory(Item item) {
		if(availableInventory - item.getWeight() >= 0) {
			quantities[item.getId()]++;
			availableInventory -= item.getWeight();
			return true;
		}
		return false;
	}
	
	public boolean addToStorage(int id) {
		Item temp = new Item(id);
		if(currentStorage.addToStorage(temp)) {
			quantities[id]--;
			availableInventory += temp.getWeight();
			if(quantities[id] <= 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addFromStorage(int id) {
		Item temp = new Item(id);
		if(addItemToInventory(temp)) {
			return currentStorage.reduceStorage(temp);
		}
		return false;
	}
	
	public void go(int direction, boolean strafe) {
		//System.out.println("going " + name);
		int followDir = -1;
		if(tail != null) {
			followDir = getDirectionToFollow();
		}
		if(!moving) {
			//System.out.println("starting " + name);
			dir = direction;
			if(theObjects[tileX + xMove[dir]][tileY + yMove[dir]] == null || theObjects[tileX + xMove[dir]][tileY + yMove[dir]] instanceof Player) {
				tileX += xMove[dir];
				if(tileX < 3 || tileX >= theMap.length - 3) {
					tileX -= xMove[dir];
					return;
				}
				tileY += yMove[dir];
				if(tileY < 3 || tileY >= theMap.length - 3) {
					tileY -= yMove[dir];
					return;
				}
				relativeX += xMove[dir];
				relativeY += yMove[dir];
				if(lead) {
					if(relativeX > 6) {
						relativeX = 6;
						x -= 64;
						tail.relativeX--;
						tail.x -= 64;
					} else if(relativeX < 3) {
						relativeX = 3;
						x += 64;
						tail.relativeX++;
						tail.x += 64;
					}
					
					if(relativeY > 6) {
						relativeY = 6;
						y -= 64;
						tail.relativeY--;
						tail.y -= 64;
					} else if(relativeY < 3) {
						relativeY = 3;
						y += 64;
						tail.relativeY++;
						tail.y += 64;
					}
				}
				
				//System.out.println(relativeX + "," + relativeY);
				newX = x + xMove[dir]*64;
				newY = y + yMove[dir]*64;
				animator.setFrames(images.get(dir));
				//System.out.println("moving");
				animator.start();
				moving = true;
				if(tail != null) {
					tail.go(followDir, strafe);
				}
			} else {
				animator.setFrames(images.get(dir));
			}
			
			
		}
		
	}
	
	public void move(int direction, boolean strafe) {
		//System.out.println("moving " + name);
		long time = System.currentTimeMillis();
		long diff = time - prevTime;
		//System.out.println(diff);
		if(diff > delayTime) {
			x += xMove[dir] * speed;
			y += yMove[dir] * speed;
			
			prevTime = time;
			//delay = delayReset;
		} 
		
		
	}
	
	public void stop() {
		//System.out.println("stopping");
		moving = false;
		animator.stop();
	}
	
	public int getDirectionToFollow() {
		if(this.tileX > tail.tileX) {
			return 0;
		} else if(this.tileX < tail.tileX) {
			return 2;
		} else if(this.tileY > tail.tileY) {
			return 1;
		} else {
			return 3;
		}
	}
	
	public void createTail() {
		this.tail = new Player(tileX - 1, tileY, false, "dr_blue", theMap, theObjects);
	}
	
	public void setLead(boolean isLead) {
		this.lead = isLead;
	}
	
	public Player swap() {
		
		if(tail != null) {
			go((dir + 2) % 4, false);
			Player temp = tail;
			tail.tail = this;
			tail = null;
			temp.setLead(true);
			this.lead = false;
			return temp;
		} else {
			return this;
		}
	}
	
	public void loadImages() {
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage ssRight = null, ssDown = null, ssLeft = null, ssUp = null;
		try {
			ssRight = loader.loadImage("images/" + name + "_right_sheet.png");
			ssDown = loader.loadImage("images/" + name + "_down_sheet.png");
			ssLeft = loader.loadImage("images/" + name + "_left_sheet.png");
			ssUp = loader.loadImage("images/" + name + "_up_sheet.png");
		} catch(IOException e) {
			e.printStackTrace();
		}
		SpriteSheet ssR = new SpriteSheet(ssRight);
		SpriteSheet ssD = new SpriteSheet(ssDown);
		SpriteSheet ssL = new SpriteSheet(ssLeft);
		SpriteSheet ssU = new SpriteSheet(ssUp);
		for(int i = 0; i < 4; i++) {
			images.get(0).add(ssR.grabSprite(i*32, 0));
			images.get(1).add(ssD.grabSprite(i*32, 0));
			images.get(2).add(ssL.grabSprite(i*32, 0));
			images.get(3).add(ssU.grabSprite(i*32, 0));
		}
		animator = new Animator();
		animator.setFrames(images.get(0));
		animator.setSpeed(100);
		//animator.start();
	}
	
	public void setEquip(int equip) {
		this.equip = equip;
	}
	
	public int getEquip() {
		return equip;
	}
	
	public BufferedImage getImage() {
		return animator.getFrame();
	}
	
	public ArrayList<Inventory> getInventory(){
		ArrayList<Inventory> inventory = new ArrayList<Inventory>();
		for(int i = 0; i < quantities.length; i++) {
			if(quantities[i] != 0) {
				inventory.add(new Inventory(i, quantities[i]));
			}
		}
		return inventory;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRX() {
		return relativeX;
	}
	
	public int getRY() {
		return relativeY;
	}
	
	public int getTileX() {
		return tileX;
	}
	
	public int getTileY() {
		return tileY;
	}

	@Override
	public void getDefaultAction(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Item> getDrops() {
		// TODO Auto-generated method stub
		return null;
	}

}
