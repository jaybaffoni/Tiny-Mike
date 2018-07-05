package com.jaybaffoni;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener{
	
	GameController controller;
	Player player;
	int size;

	public GamePanel(GameController controller) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.controller = controller;
        this.player = controller.getPlayer();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        size = 800;
    }
	
	public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }
	
	public void paintComponent(Graphics g) {
		//System.out.println("painting");
        super.paintComponent(g);       
        //paint whole board black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, size, size);
        //paint player
        g.drawImage(player.getImage(), player.getX(), player.getY(), 64, 64, null);
        player.update();
        repaint();
        
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			player.go(0, code == KeyEvent.VK_RIGHT);
		} else if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			player.go(1, code == KeyEvent.VK_DOWN);
		} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			player.go(2, code == KeyEvent.VK_LEFT);
		} else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			player.go(3, code == KeyEvent.VK_UP);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			player.stop();
		} else if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			player.stop();
		} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			player.stop();
		} else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			player.stop();
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
