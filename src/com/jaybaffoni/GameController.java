package com.jaybaffoni;

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

import com.jaybaffoni.tiles.GrassTile;
import com.jaybaffoni.tiles.LavaTile;
import com.jaybaffoni.tiles.SandTile;
import com.jaybaffoni.tiles.StoneTile;
import com.jaybaffoni.tiles.Tile;
import com.jaybaffoni.tiles.WaterTile;

public class GameController {
	
	Player player;
	GamePanel panel;
	int speed = 10;
	int mapSize = 640;
	Tile[][] map = new Tile[mapSize][mapSize];
	int visibleSize = 10;
	int lavaTiles = 0;
	
	double lowest = 0.0, highest = 0.0;
	
	public GameController() {
		createMap();
		player = new Player(10, 10, true, "dr_nitrogen", map);
		//player = new Player(mapSize / 2, mapSize / 2, true, "dr_nitrogen", map);
		player.createTail();
		
		panel = new GamePanel(this);
	}
	
	public void start() {
		
		createAndShowGUI();
	}
	
	public void createMap() {
		Noise2 perlin = new Noise2();
		lavaTiles = 0;
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				if(i < 3 || j < 3 || i >= mapSize - 3 || j >= mapSize - 3) {
					map[i][j] = new StoneTile(10);
				} else {
					double e = perlin.noise((double)i/67, (double)j/67, .25);
					double m = perlin.noise((double)i/67, (double)j/67, 1.103);
					e *= 2;
					m *= 2;
					if(m < lowest) lowest = m;
					if(m > highest) highest = m;
					//System.out.println(e);
					
					int id = getBiome2(e,m);
					//System.out.println(id);
					if(id == 9) {
						//System.out.println(i + "," + j);
					}
					if(id < 3) map[i][j] = new WaterTile(id);
					else if(id < 6) map[i][j] = new SandTile(id);
					else if(id < 9) map[i][j] = new GrassTile(id);
					else if(id < 10) map[i][j] = new LavaTile(id);
				}
				
			}
		}
		//System.out.println(lowest + "," + highest);
		//System.out.println(lavaTiles);
		if(lavaTiles < 2000) {
			createMap();
		}
	}
	
	private int getBiome2(double e, double m) {
		if(e < -0.9) return 0; //deep ocean
		else if(e < -0.7) return 1; //ocean
		else if(e < -0.5) return 2; //water
		else if(e < -0.3) return 3; //beach
		else if(e < .1) {
			if(m < -.85) {
				lavaTiles++;
				return 9; //lava
			} else if(m < .45) {
				return 4; //desert
			}
			return 5; //savannah
		} else {
			if(m < -0) {
				return 6; //plains
			} else if(m < .6) {
				return 7; //forest
			}
			return 8; //jungle
		}
	}
	
	private int getBiome(double e, double m) {
		if (e < -.3) {
			if(m < .3) return 0; //WATER - BLUE
			else return 1; //SWAMP - AQUA
		}
		else if (e < 0) {
			if(m < 0) return 2; //BEACH - YELLOW
			else if(m < .3) return 3; //DESERT - YELLOW
			else return 4; //SAVANNAH - YELLOW
		}
		else if (e < 0.3) {
			if(m < 0) return 5; //GRASS - GREEN
			else if(m < .3) return 6; //FOREST - GREEN
			else return 7; //JUNGLE - ARMY GREEN
		}
		else if (e < 0.6) {
			return 8; //MOUNTAIN - GRAY
		}
		else{
			return 9; //SNOW - WHITE
		}
	}
	
	public Tile[][] getVisibleMap(){
		//System.out.println(player.getTileX() + "," + player.getRX());
		int startX = player.getTileX() - player.getRX();
		int startY = player.getTileY() - player.getRY();
		Tile[][] toReturn = new Tile[visibleSize][visibleSize];
		
		for(int x = 0; x < 0 + visibleSize; x++) {
			for(int y = 0; y < 0 + visibleSize; y++) {
				try {
					toReturn[x][y] = map[x + startX][y + startY];
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return toReturn;
	}
	
	public Tile[][] getSurroundingMap(int size){
		//int size = 80;
		int startX = player.getTileX() - size/2;
		int startY = player.getTileY() - size/2;
		Tile[][] toReturn = new Tile[size][size];
		
		for(int x = 0; x < 0 + size; x++) {
			for(int y = 0; y < 0 + size; y++) {
				try {
					toReturn[x][y] = map[x + startX][y + startY];
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
		
		return toReturn;
	}
	
	private void createAndShowGUI() {
		
        JFrame f = new JFrame("Game");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GamePanel(this);
        f.add(panel);
        f.pack();
        f.setVisible(true);
        
        Thread thread = new Thread(){
            public void run(){
            	while(true) {
            		update();
            		try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            }
          };

          thread.start();
        
    }
	
	public void update() {
		//update player and tail
		player.update();
		int tilesToUpdate = 20;
		Tile[][] mapToUpdate = getSurroundingMap(tilesToUpdate);
		for(int i = 0; i < tilesToUpdate; i++) {
			for(int j = 0; j < tilesToUpdate; j++) {
				if(mapToUpdate[i][j] != null) {
					mapToUpdate[i][j].update();
				}
			}
		}
		
        panel.repaint();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player2) {
		this.player = player2;
	}
	
	public Tile[][] getMap(){
		return map;
	}

}
