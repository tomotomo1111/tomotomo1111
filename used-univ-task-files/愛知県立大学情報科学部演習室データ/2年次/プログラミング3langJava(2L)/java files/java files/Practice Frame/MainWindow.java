package com.zetcode;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	ScreenMode screenMode = ScreenMode.TITLE;
	
	final int WIDTH = 300;
	final int HEIGHT = 300;
	
	CardLayout layout = new CardLayout();
	
	TitlePanel titlePanel;
	GamePanel gamePanel;
	
	MainWindow() {
		
		add(new Board());
		this.setTitle("title ");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.black);
		this.setLayout(layout);
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void preparePanels() {
		
		titlePanel = new TitlePanel();
		this.add(titlePanel,"タイトル画面");
		gamePanel = new GamePanel();
		this.add(gamePanel,"ゲーム画面");
		this.pack();
	}
	
	public void prepareComponents() {
		titlePanel.prepareComponents();
		gamePanel.prepareComponents();
	}
	
	public void setFrontScreenAndFocus(ScreenMode s) {
		screenMode = s;
		
		switch(screenMode) {
		case TITLE:
			layout.show(this.getContentPane(), "タイトル画面");
			titlePanel.requestFocus();
			break;
		case GAME:
			layout.show(this.getContentPane(), "ゲーム画面");
			gamePanel.requestFocus();
			break;
		}
	}
}
