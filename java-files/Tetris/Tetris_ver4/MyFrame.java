import javax.swing.*;

import javax.swing.JFrame;

public class MyFrame extends JFrame {

    private static final int SCREEN_W = 320;
    private static final int SCREEN_H = 480;
    public static int sc_wn = SCREEN_W * 1;
    public static int sc_hn = SCREEN_H * 1;
    static JFrame myFrame = new JFrame();
    static Map map = new Map();

    public MyFrame() {
        
    }

    public static void activateFrame() {
        
        myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        myFrame.setSize(sc_wn, sc_hn);
        myFrame.getContentPane().add(map);
        myFrame.setVisible(true);
        myFrame.setLocationRelativeTo(null);
    }

    public static void setFrameSize(int level_x, int level_y) {

        sc_wn = SCREEN_W * level_x;
        sc_hn = SCREEN_H * level_y;
        myFrame.setSize(sc_wn, sc_hn);
    }

    public static void main(String[] args) {

        activateFrame();
        inGame();
    }

    public static void inGame() {
        map.mainLoop();
    }
}