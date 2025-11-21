import javax.swing.*;

public class MyFrame extends JFrame {

    private static final int SCREEN_W = 500;
    private static final int SCREEN_H = 340;
    private static final int SCREEN_W_FRAME_EDGE = 16;
    private static final int SCREEN_H_FRAME_EDGE = 39;
    private static final int MARGIN = 10;
    private static JFrame frame = new JFrame();
    private static Map map;
    
    public static void MyFrame() {
        
    }

    public static void setFrame() {
        map = new Map(SCREEN_W, SCREEN_H, MARGIN);
        map.initMap();
        frame.setResizable(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(SCREEN_W + SCREEN_W_FRAME_EDGE, SCREEN_H + SCREEN_H_FRAME_EDGE);
        frame.getContentPane().add(map);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        setFrame();
        map.loop();
    }
}
