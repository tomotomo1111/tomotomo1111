package com.zetcode;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Snake extends JFrame {
	
    public Snake() {
    	
    	initUI();
    	
    }
    
    private void initUI() {
        
        add(new Board());
        
        setResizable(false);
        pack();
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public static void main(String[] args) {
        /*
    	mainWindow = new MainWindow();
    	mainWindow.preparePanels();
    	mainWindow.prepareComponents();
    	mainWindow.setFrontScreenAndFocus(ScreenMode.TITLE);
    	mainWindow.setVisible(true);
    	*/
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
        
    }
}