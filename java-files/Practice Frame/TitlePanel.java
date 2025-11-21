package com.zetcode;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePanel extends JPanel{
	private static final long serialBersionUID = 1L;
	
	JLabel titleLabel;
	
	TitlePanel() {
		
		this.setLayout(null);
		this.setBackground(Color.black);
		
	}
	
	public void prepareComponents() {
		
		titleLabel = new JLabel();
		titleLabel.setText("タイトル画面");
		titleLabel.setBounds(100,0,100,30);
		this.add(titleLabel);
	}
}
