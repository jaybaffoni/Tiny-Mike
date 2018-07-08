package com.jaybaffoni;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jaybaffoni.tiles.Tile;

public class GamePanel extends JPanel implements KeyListener{
	
	GameController controller;
	Player player;
	int size;
	BufferedImage[] textures = new BufferedImage[16];
	Tile[][] partialMap;
	int zoomLevel = 0;
	int[] zoomMapSizes = {10,20,40,80,160};
	int[] zoomSquareSizes = {64, 32, 16, 8, 4};
	enum State
	{
	    PLAY, INVENTORY;
	}
	State state = State.PLAY;
	BufferedImage[] items = new BufferedImage[64];

	public GamePanel(GameController controller) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.controller = controller;
        this.player = controller.getPlayer();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        size = 640;
        loadImages();
    }
	
	public void loadImages() {
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage textureImage = null;
		BufferedImage itemsImage = null;
		try {
			textureImage = loader.loadImage("images/texturesSheet2.png");
			itemsImage = loader.loadImage("images/itemsSheet.png");
		} catch(IOException e) {
			e.printStackTrace();
		}
		SpriteSheet textureSheet = new SpriteSheet(textureImage);
		SpriteSheet itemsSheet = new SpriteSheet(itemsImage);
		int count = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				textures[count] = (textureSheet.grabSprite(j*32, i*32));
				count++;
			}
			
		}
		count = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				items[count] = itemsSheet.grabSprite(j*32, i*32);
				count++;
			}
		}
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,size,size);
		int mapSize = zoomMapSizes[zoomLevel];
		int squareSize = zoomSquareSizes[zoomLevel];
		boolean showPlayer = false;
		if(zoomLevel == 0) {
			showPlayer = true;
		}
		
		/*if(state == State.ZOOM1) {
			partialMap = controller.getSurroundingMap(40);
			mapSize = 40;
			squareSize = 16;
			showPlayer = false;
		} else if(state == State.ZOOM2) {
			partialMap = controller.getSurroundingMap(160);
			mapSize = 160;
			squareSize = 4;
			showPlayer = false;
		}else if(state == State.PLAY) {
			partialMap = controller.getVisibleMap();
			mapSize = 10;
			squareSize = 64;
			showPlayer = true;
		}*/
		if(zoomLevel == 0) {
			partialMap = controller.getVisibleMap();
		} else {
			partialMap = controller.getSurroundingMap(mapSize);
		}
        for(int x = 0; x < mapSize; x++) {
        	for(int y = 0; y < mapSize; y++) {
        		if(partialMap[x][y] != null)
        		g.drawImage(textures[partialMap[x][y].getId()], x*squareSize, y*squareSize, squareSize, squareSize, null);
        		if(showPlayer) {
        			if(partialMap[x][y].getDrop() != null) {
        				if(partialMap[x][y].getDrop().isVisible()) {
        					g.drawImage(items[partialMap[x][y].getDrop().getId()], x*squareSize, y*squareSize, squareSize, squareSize, null);
        				}
	        			
	        		}
        		}
        		
        	}
        }
        //paint player
        if(showPlayer) {
        	if(player.tail != null) {
        		//System.out.println("not null");
        		g.drawImage(player.tail.getImage(), player.tail.getX(), player.tail.getY(), squareSize, squareSize, null);
        	}
        	g.drawImage(player.getImage(), player.getX(), player.getY(), squareSize, squareSize, null);
        	if(player.isAcquiring()) {
        		//System.out.println("acquiring");
        		g.drawImage(items[player.recentDrop.getId()], player.getX() + squareSize/4, player.getY() + squareSize/4, squareSize/2, squareSize/2, null);
        	}
        	
        }
        //player.update();
        //repaint();
        
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(zoomLevel == 0) {
			if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
				player.go(0, code == KeyEvent.VK_RIGHT);
			} else if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
				player.go(1, code == KeyEvent.VK_DOWN);
			} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
				player.go(2, code == KeyEvent.VK_LEFT);
			} else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
				player.go(3, code == KeyEvent.VK_UP);
			} else if(code == KeyEvent.VK_SHIFT) {
				player = player.swap();
				controller.setPlayer(player);
			} else if(code == KeyEvent.VK_SPACE) {
				player.action();
			}
		}
		if(code == KeyEvent.VK_O) {
			//zoom in
			zoomLevel++;
			if(zoomLevel >= zoomMapSizes.length) {
				zoomLevel = zoomMapSizes.length - 1;
			}
		} else if(code == KeyEvent.VK_I) {
			//zoom out
			zoomLevel--;
			if(zoomLevel < 0) {
				zoomLevel = 0;
			}
		}
		
		
		/*if(code == KeyEvent.VK_Z) {
			//System.out.println("pressed");
			if(state == State.PLAY) {
				state = State.ZOOM1;
			} else if(state == State.ZOOM1) {
				state = State.ZOOM2;
			} else if(state == State.ZOOM2) {
				state = State.PLAY;
			}
		} */
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		/*if(code == KeyEvent.VK_M) {
			System.out.println("released");
			state = State.PLAY;
		} else if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			player.stop();
		} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			player.stop();
		} else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			player.stop();
		}*/
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
