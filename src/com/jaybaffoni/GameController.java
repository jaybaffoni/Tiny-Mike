package com.jaybaffoni;

import javax.swing.JFrame;

public class GameController {
	
	Player player;
	GamePanel panel;
	int speed = 100;
	
	public GameController() {
		player = new Player(10,10);
		panel = new GamePanel(this);
	}
	
	public void start() {
		createAndShowGUI();
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
            		
                }
            }
          };

          thread.start();
        
    }
	
	public Player getPlayer() {
		return player;
	}

}
