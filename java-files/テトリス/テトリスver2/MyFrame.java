import javax.swing.*;

import javax.swing.JFrame;

public class MyFrame extends JFrame {

    public static final int SCREEN_W = 320;
    public static final int SCREEN_H = 480;
    static JFrame myFrame = new JFrame();
    static Map map = new Map();

    public MyFrame() {
        
    }

    public static void activateFrame() {
        
        myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        myFrame.setSize(SCREEN_W, SCREEN_H);
        myFrame.getContentPane().add(map);
        myFrame.setVisible(true);
        myFrame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        activateFrame();
        inGame();
    }

    public static void inGame() {
        map.mainLoop();
    }
}