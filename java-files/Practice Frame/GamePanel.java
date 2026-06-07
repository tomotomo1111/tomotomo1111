package com.zetcode;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	private static final long serialBersionUID = 1L;
	
	JLabel gameLabel;
	
	GamePanel() {
		
		this.setLayout(null);
		this.setBackground(Color.yellow);
		
	}
	
	public void prepareComponents() {
		
		gameLabel = new JLabel();
		gameLabel.setText("ゲーム画面");
		gameLabel.setBounds(100,0,100,30);
		this.add(gameLabel);
	}
}
