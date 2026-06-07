import javax.swing.JFrame;

public class MyFrame extends JFrame {

    public static final int SCREEN_W = 320;
    public static final int SCREEN_H = 480;
    public static final int FRAME_OFFSET_X = 16;
    public static final int FRAME_OFFSET_Y = 39;
    public static final int SC_WN_DEFAULT = (int) (SCREEN_W * 1 /* players */ * 1.0 /* SIZE_MULTIPILER_X*/ + FRAME_OFFSET_X);
    public static final int SC_HN_DEFAULT = (int) (SCREEN_H * 1 * 1.0 + FRAME_OFFSET_Y);
    public static int sc_wn = SC_WN_DEFAULT;
    public static int sc_hn = SC_HN_DEFAULT;
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

    public static void setFrameSize(int level_x, int level_y, double size_pro_x, double size_pro_y) {

        sc_wn = (int) (SCREEN_W * level_x * size_pro_x + FRAME_OFFSET_X);
        sc_hn = (int) (SCREEN_H * level_y * size_pro_y + FRAME_OFFSET_Y);
        myFrame.setSize(sc_wn, sc_hn);
    }

    public static void main(String[] args) {

        activateFrame();
        inGame();
    }

    public static void inGame() {
        map.mainLoop();
    }

    public static double getFrameWidthPro() {
        return myFrame.getSize().width / (double) SC_WN_DEFAULT;
    }

    public static double getFrameHeightPro() {
        return myFrame.getSize().height / (double) SC_HN_DEFAULT;
    }

    public static void fixFrameSizeField() {
        sc_wn = myFrame.getSize().width;
        sc_hn = myFrame.getSize().height;
    }
}