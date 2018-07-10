package com.jaybaffoni;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jaybaffoni.entities.Entity;
import com.jaybaffoni.tiles.Tile;

public class GamePanel extends JPanel implements KeyListener{
	
	GameController controller;
	Player player;
	int size;
	BufferedImage[] textures = new BufferedImage[16];
	Tile[][] partialMap;
	Entity[][] partialObjects;
	int zoomLevel = 0;
	int[] zoomMapSizes = {10,20,40,80,160};
	int[] zoomSquareSizes = {64, 32, 16, 8, 4};
	enum State
	{
	    PLAY, INVENTORY, STORAGE;
	}
	State state = State.PLAY;
	BufferedImage[] items = new BufferedImage[64];
	BufferedImage[] objects = new BufferedImage[64];
	BufferedImage marker;
	int startInventoryAt = 0;
	int currentInventoryIndex = 0;
	boolean markerInInventory = true;
	ArrayList<Inventory> currentInventory;
	ArrayList<Inventory> currentStorage;
	int inventoryOffset = 0;
	Font font = new Font( "Monospaced", Font.PLAIN, 16 );
	Font bigFont = new Font( "Monospaced", Font.PLAIN, 24 );
    

	public GamePanel(GameController controller) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.controller = controller;
        this.player = controller.getPlayer();
        currentInventory = player.getInventory();
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
		BufferedImage objectsImage = null;
		try {
			textureImage = loader.loadImage("images/texturesSheet2.png");
			itemsImage = loader.loadImage("images/itemsSheet.png");
			marker = loader.loadImage("images/marker.png");
			objectsImage = loader.loadImage("images/objects.png");
		} catch(IOException e) {
			e.printStackTrace();
		}
		SpriteSheet textureSheet = new SpriteSheet(textureImage);
		SpriteSheet itemsSheet = new SpriteSheet(itemsImage);
		SpriteSheet objectsSheet = new SpriteSheet(objectsImage);
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
				objects[count] = objectsSheet.grabSprite(j*32, i*32);
				count++;
			}
		}
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(size, size + 64);
    }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,size,size + 64);
		int mapSize = zoomMapSizes[zoomLevel];
		int squareSize = zoomSquareSizes[zoomLevel];
		boolean showPlayer = false;
		if(zoomLevel == 0) {
			showPlayer = true;
		}
		if(zoomLevel == 0) {
			partialMap = controller.getVisibleMap();
			partialObjects = controller.getVisibleObjects();
		} else {
			partialMap = controller.getSurroundingMap(mapSize);
		}
        for(int x = 0; x < mapSize; x++) {
        	for(int y = 0; y < mapSize; y++) {
        		if(partialMap[x][y] != null)
        		g.drawImage(textures[partialMap[x][y].getId()], x*squareSize, y*squareSize, squareSize, squareSize, null);
        		if(showPlayer) {
        			if(!partialMap[x][y].getDrops().isEmpty()) {
        				g.drawImage(items[partialMap[x][y].getDrop().getId()], x*squareSize, y*squareSize, squareSize, squareSize, null);
        			
	        		}
        			if(partialObjects[x][y] != null && !(partialObjects[x][y] instanceof Player)) {
        				g.drawImage(objects[partialObjects[x][y].getId()], x*squareSize, y*squareSize, squareSize, squareSize, null);
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
        //paint equip
        g.setColor(Color.white);
        g.drawRect(0, size, 64, 64);
        if(player.getEquip() >= 0) {
        	g.drawImage(items[player.getEquip()], 16, size, 32, 32, null);
        }
        
        if(player.currentStorage != null) {
        	state = State.STORAGE;
        }
        if(state == State.INVENTORY || state == State.STORAGE) {
        	g.setColor(Color.BLACK);
        	g.fillRect(6*64, 0, 4*64, 640);
        	g.setColor(Color.white);
        	g.setFont(bigFont);
        	g.drawString("Inventory: " + (player.totalInventory - player.availableInventory) + "/" + player.totalInventory, 7*64 - 48, 32);
        	g.setFont(font);
        	currentInventory = player.getInventory();
        	if(state == State.STORAGE) {
        		currentStorage = player.currentStorage.getStorage();
	        	if(markerInInventory) {
	        		if(currentInventory.isEmpty() && !currentStorage.isEmpty()) {
	        			markerInInventory = false;
	        		}
	        	} else {
	        		if(currentStorage.isEmpty() && !currentInventory.isEmpty()) {
	        			markerInInventory = true;
	        		}
	        	}
        	}
        	
        	
        	int startY = 48;
        	int x = (6*64) + 32;
        	for(int i = 0; i < 18; i++) {
        		if(i < currentInventory.size()) {
        			g.setColor(Color.white);
        			g.drawRect(6*64, startY * (i + 1), 4*64, 48);
        			g.drawImage(items[currentInventory.get(i).getId()], x, startY * (i + 1) + 8, 32, 32, null);
        			String quantity;
            		if(currentInventory.get(i).getQuantity() < 10){
            			quantity = "0" + currentInventory.get(i).getQuantity();
            		} else {
            			quantity = currentInventory.get(i).getQuantity() + "";
            		}
            		
            		g.drawString("x" + quantity, 6*64 + 72, (i + 1) * 48 + 20);
            		g.drawString(currentInventory.get(i).getName(), 6*64 + 72, (i + 1) * 48 + 36);
        		} else {
        			break;
        		}
        		
        	}
        	if(currentInventory.size() > 0) {
        		if(markerInInventory)
        			g.drawImage(marker, 6*64, (currentInventoryIndex + 1) * 48 + 8, 32, 32, null);
        	}
        	
        }
        if(state == State.STORAGE) {
        	g.setColor(Color.BLACK);
        	g.fillRect(0, 0, 4*64, 640);
        	g.setColor(Color.white);
        	g.setFont(bigFont);
        	g.drawString("Storage: " + (player.currentStorage.originalCapacity - player.currentStorage.currentAvailability) + "/" + player.currentStorage.originalCapacity, 16, 32);
        	g.setFont(font);
        	
        	
        	int startY = 48;
        	int x = 32;
        	for(int i = 0; i < 18; i++) {
        		if(i < currentStorage.size()) {
        			g.setColor(Color.white);
        			g.drawRect(0, startY * (i + 1), 4*64, 48);
        			g.drawImage(items[currentStorage.get(i).getId()], x, startY * (i + 1) + 8, 32, 32, null);
        			String quantity;
            		if(currentStorage.get(i).getQuantity() < 10){
            			quantity = "0" + currentStorage.get(i).getQuantity();
            		} else {
            			quantity = currentStorage.get(i).getQuantity() + "";
            		}
            		
            		g.drawString("x" + quantity, 0 + 72, (i + 1) * 48 + 20);
            		g.drawString(currentStorage.get(i).getName(), 0 + 72, (i + 1) * 48 + 36);
        		} else {
        			break;
        		}
        		
        	}
        	if(currentStorage.size() > 0) {
        		if(!markerInInventory)
        			g.drawImage(marker, 0, (currentInventoryIndex + 1) * 48 + 8, 32, 32, null);
        	}
        }
        
        
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(state == State.PLAY) {
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
					player.harvest();
				} else if(code == KeyEvent.VK_ENTER) {
					player.action();
				}
			}
		} else if(state == State.INVENTORY) {
			currentInventory = player.getInventory();
			if(code == KeyEvent.VK_SHIFT) {
				player = player.swap();
				controller.setPlayer(player);
			} else if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
				currentInventoryIndex++;
				if(currentInventoryIndex >= currentInventory.size()) {
					currentInventoryIndex = currentInventory.size() - 1;
				}
			}  else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
				currentInventoryIndex--;
				if(currentInventoryIndex < 0) {
					currentInventoryIndex = 0;
				}
			} else if(code == KeyEvent.VK_E) {
				player.setEquip(currentInventory.get(currentInventoryIndex).getId());
			}
		} else if(state == State.STORAGE) {
			currentInventory = player.getInventory();
			currentStorage = player.currentStorage.getStorage();
			if(code == KeyEvent.VK_SHIFT) {
				player = player.swap();
				controller.setPlayer(player);
			} else if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
				currentInventoryIndex++;
				if(markerInInventory) {
					if(currentInventoryIndex >= currentInventory.size()) {
						currentInventoryIndex = currentInventory.size() - 1;
					}
				} else {
					if(currentInventoryIndex >= currentStorage.size()) {
						currentInventoryIndex = currentStorage.size() - 1;
					}
				}
				
			}  else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
				currentInventoryIndex--;
				if(currentInventoryIndex < 0) {
					currentInventoryIndex = 0;
				}
			}  else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
				if(markerInInventory && currentStorage.size() > 0) {
					markerInInventory = false;
					if(currentInventoryIndex >= currentStorage.size()) {
						currentInventoryIndex = currentStorage.size() - 1;
					}
				}
			}  else if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
				if(!markerInInventory && currentInventory.size() > 0) {
					markerInInventory = true;
					if(currentInventoryIndex >= currentInventory.size()) {
						currentInventoryIndex = currentInventory.size() - 1;
					}
				}
			}  else if(code == KeyEvent.VK_E) {
				if(markerInInventory) {
					player.setEquip(currentInventory.get(currentInventoryIndex).getId());
				}
			}  else if(code == KeyEvent.VK_SPACE) {
				if(markerInInventory) {
					if(currentInventory.size() > 0) {
						//marker is showing
						if(player.addToStorage(currentInventory.get(currentInventoryIndex).getId())) {
							if(currentInventoryIndex > 0) {
								currentInventoryIndex--;
							}
						}
						
					}
				} else {
					if(currentStorage.size() > 0) {
						//marker is showing
						if(player.addFromStorage(currentStorage.get(currentInventoryIndex).getId())) {
							if(currentInventoryIndex > 0) {
								currentInventoryIndex--;
							}
						}
					}
				}
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
		} else if(code == KeyEvent.VK_X) {
			if(state == State.PLAY) {
				state = State.INVENTORY;
			} else {
				if(player.currentStorage != null) {
					player.currentStorage = null;
					player.tail.currentStorage = null;
				}
				state = State.PLAY;
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
