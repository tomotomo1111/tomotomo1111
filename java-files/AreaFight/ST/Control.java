import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Control extends JPanel implements ActionListener {

    private static final int SLIDER_WIDTH = 300;
    private static final int SLIDER_HEIGHT = 50;
    private static final int SLIDER_LEFTMARGIN = 10;
    private static final int SLIDER_TOPMARGIN = 10;
    private static final int BUTTON_WIDTH = 30;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_HORIZONTAL_MARGIN = BUTTON_WIDTH + 0;
    private static final int BUTTON_VERTICAL_MARGIN = BUTTON_HEIGHT + 0;
    private static final int BUTTON_COL_AND_DIR_MARGIN = 10;
    private static final int Button_AND_SLIDER_MARGIN = 10;

    private static JButton button_NW, button_NE, button_SW, button_SE;
    private static JButton button_R, button_B, button_G, button_Y;
    private static JButton button_BulNormal, button_BulStrong;
    public static JButton button_START, button_STOP, button_CURRENT_RESET, button_ALL_RESET;

    private static final int BUL_FPW = 50; // 1 / 2
    private static final int BUL_FSP = 50; // 1 / 100
    private static final int BUL_FBL = 50; // +1
    private static final int BUL_FFR = 50; // 101-
    private static final int BUL_FRN = 50; // 1 / 100 * Math.PI
    private static final int BUL_FAX = 50; // (-50 / 50.0) * Math.PI / 4
    public static double bul_fpw_real = BUL_FPW / 2 + 1;
    public static double bul_fsp_real = BUL_FSP / 100.0;
    public static double bul_fbl_real = BUL_FBL + 1;
    public static double bul_ffr_real = 101 - BUL_FFR;
    public static double bul_frn_real = Math.PI * BUL_FRN / 100.0;
    public static double bul_fax_real = (Math.PI * 0.26) * ((BUL_FAX - 50) / 50.0);
    public static double bul_NW_pw, bul_NE_pw, bul_SW_pw, bul_SE_pw;
    public static double bul_NW_sp, bul_NE_sp, bul_SW_sp, bul_SE_sp;
    public static double bul_NW_bl, bul_NE_bl, bul_SW_bl, bul_SE_bl;
    public static double bul_NW_fr, bul_NE_fr, bul_SW_fr, bul_SE_fr;
    public static double bul_NW_rn, bul_NE_rn, bul_SW_rn, bul_SE_rn;
    public static double bul_NW_ax, bul_NE_ax, bul_SW_ax, bul_SE_ax;

    private JSlider slider_pw;
    private JSlider slider_sp;
    private JSlider slider_bl;
    private JSlider slider_fr;
    private JSlider slider_rn;
    private JSlider slider_ax;
    private ArrayList<JSlider> slider_array = new ArrayList<>();
    private ArrayList<String> slidername_array = new ArrayList<>();
    static EnumDirMode.DirMode dirMode = EnumDirMode.DirMode.NW;

    private static final Color COLOR_SLIDER = new Color(22, 22, 22);
    private static final Color COLOR_FNW = Color.RED;
    private static final Color COLOR_FNE = Color.BLUE;
    private static final Color COLOR_FSW = Color.GREEN;
    private static final Color COLOR_FSE = Color.YELLOW;
    public static Color color_NW = COLOR_FNW;
    public static Color color_NE = COLOR_FNE;
    public static Color color_SW = COLOR_FSW;
    public static Color color_SE = COLOR_FSE;

    private static final String BULLET_FMODE = "NORMAL";
    public static String bullet_NW, bullet_NE, bullet_SW, bullet_SE;

    static boolean start_button_frag = true;
    static boolean color_button_frag = true;
    static boolean stop_button_frag = false;
    static boolean reset_button_frag = true;

    static int timer = 0;

    public void initControl() {
        removeAll();

        bul_NW_pw = bul_NE_pw = bul_SW_pw = bul_SE_pw = bul_fpw_real;
        bul_NW_sp = bul_NE_sp = bul_SW_sp = bul_SE_sp = bul_fsp_real;
        bul_NW_bl = bul_NE_bl = bul_SW_bl = bul_SE_bl = bul_fbl_real;
        bul_NW_fr = bul_NE_fr = bul_SW_fr = bul_SE_fr = bul_ffr_real;
        bul_NW_rn = bul_NE_rn = bul_SW_rn = bul_SE_rn = bul_frn_real;
        bul_NW_ax = bul_NE_ax = bul_SW_ax = bul_SE_ax = bul_fax_real;
        bullet_NW = bullet_NE = bullet_SW = bullet_SE = BULLET_FMODE;

        slider_pw = new JSlider(0, 100, BUL_FPW);
        this.add(slider_pw);
        slider_pw.setBounds(SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN + SLIDER_HEIGHT * 0, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider_pw.setMajorTickSpacing(10);
        slider_pw.setPaintTicks(true);
        slider_pw.setPaintTrack(false);
        slider_pw.setForeground(Color.WHITE);
        slider_pw.setBackground(COLOR_SLIDER);
        slider_pw.setLabelTable(slider_pw.createStandardLabels(20));
        slider_pw.setPaintLabels(true);
        slider_array.add(slider_pw);
        slidername_array.add("slider_pw");

        slider_sp = new JSlider(0, 100, BUL_FSP);
        this.add(slider_sp);
        slider_sp.setBounds(SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN + SLIDER_HEIGHT * 1, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider_sp.setMajorTickSpacing(10);
        slider_sp.setPaintTicks(true);
        slider_sp.setPaintTrack(false);
        slider_sp.setForeground(Color.WHITE);
        slider_sp.setBackground(COLOR_SLIDER);
        slider_sp.setLabelTable(slider_sp.createStandardLabels(20));
        slider_sp.setPaintLabels(true);
        slider_array.add(slider_sp);
        slidername_array.add("slider_sp");

        slider_bl = new JSlider(0, 100, BUL_FBL);
        this.add(slider_bl);
        slider_bl.setBounds(SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN + SLIDER_HEIGHT * 2, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider_bl.setMajorTickSpacing(10);
        slider_bl.setPaintTicks(true);
        slider_bl.setPaintTrack(false);
        slider_bl.setForeground(Color.WHITE);
        slider_bl.setBackground(COLOR_SLIDER);
        slider_bl.setLabelTable(slider_bl.createStandardLabels(20));
        slider_bl.setPaintLabels(true);
        slider_array.add(slider_bl);
        slidername_array.add("slider_bl");

        slider_fr = new JSlider(0, 100, BUL_FFR);
        this.add(slider_fr);
        slider_fr.setBounds(SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN + SLIDER_HEIGHT * 3, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider_fr.setMajorTickSpacing(10);
        slider_fr.setPaintTicks(true);
        slider_fr.setPaintTrack(false);
        slider_fr.setForeground(Color.WHITE);
        slider_fr.setBackground(COLOR_SLIDER);
        slider_fr.setLabelTable(slider_fr.createStandardLabels(20));
        slider_fr.setPaintLabels(true);
        slider_array.add(slider_fr);
        slidername_array.add("slider_fr");

        slider_rn = new JSlider(0, 100, BUL_FRN);
        this.add(slider_rn);
        slider_rn.setBounds(SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN + SLIDER_HEIGHT * 4, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider_rn.setMajorTickSpacing(10);
        slider_rn.setPaintTicks(true);
        slider_rn.setPaintTrack(false);
        slider_rn.setForeground(Color.WHITE);
        slider_rn.setBackground(COLOR_SLIDER);
        slider_rn.setLabelTable(slider_rn.createStandardLabels(20));
        slider_rn.setPaintLabels(true);
        slider_array.add(slider_rn);
        slidername_array.add("slider_rn");

        slider_ax = new JSlider(0, 100, BUL_FAX);
        this.add(slider_ax);
        slider_ax.setBounds(SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN + SLIDER_HEIGHT * 5, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider_ax.setMajorTickSpacing(10);
        slider_ax.setPaintTicks(true);
        slider_ax.setPaintTrack(false);
        slider_ax.setForeground(Color.WHITE);
        slider_ax.setBackground(COLOR_SLIDER);
        slider_ax.setLabelTable(slider_ax.createStandardLabels(20));
        slider_ax.setPaintLabels(true);
        slider_array.add(slider_ax);
        slidername_array.add("slider_ax");

        int button_pivot_y = SLIDER_TOPMARGIN + SLIDER_HEIGHT * 6 + Button_AND_SLIDER_MARGIN * 3;

        button_NW = new JButton("NW");
        this.add(button_NW);
        button_NW.setMargin(new Insets(0, 0, 0, 0));
        button_NW.setBounds(SLIDER_LEFTMARGIN + BUTTON_HORIZONTAL_MARGIN * 0,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        // button_NW.setForeground(color_NW);
        button_NW.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                button_NW.setEnabled(false);
                button_NE.setEnabled(true);
                button_SW.setEnabled(true);
                button_SE.setEnabled(true);
                slider_pw.setValue(((int) bul_NW_pw - 1) * 2);
                slider_sp.setValue((int) (bul_NW_sp * 100));
                slider_bl.setValue((int) bul_NW_bl - 1);
                slider_fr.setValue(101 - (int) bul_NW_fr);
                slider_rn.setValue((int) (bul_NW_rn / Math.PI * 100));
                slider_ax.setValue((int) ((bul_NW_ax / (Math.PI * 0.26)) * 50 + 50));
                repaint();
                dirMode = EnumDirMode.DirMode.NW;
            }
        });

        button_NE = new JButton("NE");
        this.add(button_NE);
        button_NE.setMargin(new Insets(0, 0, 0, 0));
        button_NE.setBounds(SLIDER_LEFTMARGIN + BUTTON_HORIZONTAL_MARGIN * 1,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        button_NE.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                button_NW.setEnabled(true);
                button_NE.setEnabled(false);
                button_SW.setEnabled(true);
                button_SE.setEnabled(true);
                slider_pw.setValue(((int) bul_NE_pw - 1) * 2);
                slider_sp.setValue((int) (bul_NE_sp * 100));
                slider_bl.setValue((int) bul_NE_bl - 1);
                slider_fr.setValue(101 - (int) bul_NE_fr);
                slider_rn.setValue((int) (bul_NE_rn / Math.PI * 100));
                slider_ax.setValue((int) ((bul_NE_ax / (Math.PI * 0.26)) * 50 + 50));
                repaint();
                dirMode = EnumDirMode.DirMode.NE;
            }
        });

        button_SW = new JButton("SW");
        this.add(button_SW);
        button_SW.setMargin(new Insets(0, 0, 0, 0));
        button_SW.setBounds(SLIDER_LEFTMARGIN + BUTTON_HORIZONTAL_MARGIN * 0,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 1, BUTTON_WIDTH, BUTTON_HEIGHT);
        button_SW.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                button_NW.setEnabled(true);
                button_NE.setEnabled(true);
                button_SW.setEnabled(false);
                button_SE.setEnabled(true);
                slider_pw.setValue(((int) bul_SW_pw - 1) * 2);
                slider_sp.setValue((int) (bul_SW_sp * 100));
                slider_bl.setValue((int) bul_SW_bl - 1);
                slider_fr.setValue(101 - (int) bul_SW_fr);
                slider_rn.setValue((int) (bul_SW_rn / Math.PI * 100));
                slider_ax.setValue((int) ((bul_SW_ax / (Math.PI * 0.26)) * 50 + 50));
                repaint();
                dirMode = EnumDirMode.DirMode.SW;
            }
        });

        button_SE = new JButton("SE");
        this.add(button_SE);
        button_SE.setMargin(new Insets(0, 0, 0, 0));
        button_SE.setBounds(SLIDER_LEFTMARGIN + BUTTON_HORIZONTAL_MARGIN * 1,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 1, BUTTON_WIDTH, BUTTON_HEIGHT);
        button_SE.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                button_NW.setEnabled(true);
                button_NE.setEnabled(true);
                button_SW.setEnabled(true);
                button_SE.setEnabled(false);
                slider_pw.setValue(((int) bul_SE_pw - 1) * 2);
                slider_sp.setValue((int) (bul_SE_sp * 100));
                slider_bl.setValue((int) bul_SE_bl - 1);
                slider_fr.setValue(101 - (int) bul_SE_fr);
                slider_rn.setValue((int) (bul_SE_rn / Math.PI * 100));
                slider_ax.setValue((int) ((bul_SE_ax / (Math.PI * 0.26)) * 50 + 50));
                repaint();
                dirMode = EnumDirMode.DirMode.SE;
            }
        });

        button_R = new JButton("RE");
        this.add(button_R);
        button_R.setMargin(new Insets(0, 0, 0, 0));
        button_R.setBounds(
                SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 1 + BUTTON_HORIZONTAL_MARGIN * 1 + BUTTON_COL_AND_DIR_MARGIN,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_R.setForeground(Color.RED);
        button_R.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (dirMode.equals(EnumDirMode.DirMode.NW))
                    color_NW = Color.RED;
                if (dirMode.equals(EnumDirMode.DirMode.NE))
                    color_NE = Color.RED;
                if (dirMode.equals(EnumDirMode.DirMode.SW))
                    color_SW = Color.RED;
                if (dirMode.equals(EnumDirMode.DirMode.SE))
                    color_SE = Color.RED;
                repaint();
            }
        });

        button_B = new JButton("BL");
        this.add(button_B);
        button_B.setMargin(new Insets(0, 0, 0, 0));
        button_B.setBounds(
                SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 1 + BUTTON_HORIZONTAL_MARGIN * 2 + BUTTON_COL_AND_DIR_MARGIN,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_B.setForeground(Color.BLUE);
        button_B.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (dirMode.equals(EnumDirMode.DirMode.NW))
                    color_NW = Color.BLUE;
                if (dirMode.equals(EnumDirMode.DirMode.NE))
                    color_NE = Color.BLUE;
                if (dirMode.equals(EnumDirMode.DirMode.SW))
                    color_SW = Color.BLUE;
                if (dirMode.equals(EnumDirMode.DirMode.SE))
                    color_SE = Color.BLUE;
                repaint();
            }
        });

        button_G = new JButton("GR");
        this.add(button_G);
        button_G.setMargin(new Insets(0, 0, 0, 0));
        button_G.setBounds(
                SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 1 + BUTTON_HORIZONTAL_MARGIN * 1 + BUTTON_COL_AND_DIR_MARGIN,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 1, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_G.setForeground(Color.GREEN);
        button_G.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (dirMode.equals(EnumDirMode.DirMode.NW))
                    color_NW = Color.GREEN;
                if (dirMode.equals(EnumDirMode.DirMode.NE))
                    color_NE = Color.GREEN;
                if (dirMode.equals(EnumDirMode.DirMode.SW))
                    color_SW = Color.GREEN;
                if (dirMode.equals(EnumDirMode.DirMode.SE))
                    color_SE = Color.GREEN;
                repaint();
            }
        });

        button_Y = new JButton("YL");
        this.add(button_Y);
        button_Y.setMargin(new Insets(0, 0, 0, 0));
        button_Y.setBounds(
                SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 1 + BUTTON_HORIZONTAL_MARGIN * 2 + BUTTON_COL_AND_DIR_MARGIN,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 1, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_Y.setForeground(Color.YELLOW);
        button_Y.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (dirMode.equals(EnumDirMode.DirMode.NW))
                    color_NW = Color.YELLOW;
                if (dirMode.equals(EnumDirMode.DirMode.NE))
                    color_NE = Color.YELLOW;
                if (dirMode.equals(EnumDirMode.DirMode.SW))
                    color_SW = Color.YELLOW;
                if (dirMode.equals(EnumDirMode.DirMode.SE))
                    color_SE = Color.YELLOW;
                repaint();
            }
        });

        button_BulNormal = new JButton("NORMAL");
        this.add(button_BulNormal);
        button_BulNormal.setMargin(new Insets(0, 0, 0, 0));
        button_BulNormal.setBounds(
                SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 2 + BUTTON_HORIZONTAL_MARGIN * 2 + BUTTON_COL_AND_DIR_MARGIN * 2,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_BulNormal.setForeground(Color.WHITE);
        button_BulNormal.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (dirMode.equals(EnumDirMode.DirMode.NW))
                    bullet_NW = "NORMAL";
                if (dirMode.equals(EnumDirMode.DirMode.NE))
                    bullet_NE = "NORMAL";
                if (dirMode.equals(EnumDirMode.DirMode.SW))
                    bullet_SW = "NORMAL";
                if (dirMode.equals(EnumDirMode.DirMode.SE))
                    bullet_SE = "NORMAL";
                repaint();
            }
        });

        button_BulStrong = new JButton("STRONG");
        this.add(button_BulStrong);
        button_BulStrong.setMargin(new Insets(0, 0, 0, 0));
        button_BulStrong.setBounds(
                SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 2 + BUTTON_HORIZONTAL_MARGIN * 2 + BUTTON_COL_AND_DIR_MARGIN * 2,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 1, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_BulStrong.setForeground(Color.WHITE);
        button_BulStrong.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (dirMode.equals(EnumDirMode.DirMode.NW))
                    bullet_NW = "STRONG";
                if (dirMode.equals(EnumDirMode.DirMode.NE))
                    bullet_NE = "STRONG";
                if (dirMode.equals(EnumDirMode.DirMode.SW))
                    bullet_SW = "STRONG";
                if (dirMode.equals(EnumDirMode.DirMode.SE))
                    bullet_SE = "STRONG";
                repaint();
            }
        });

        button_START = new JButton("START");
        this.add(button_START);
        button_START.setMargin(new Insets(0, 0, 0, 0));
        button_START.setBounds(SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 6 + BUTTON_HORIZONTAL_MARGIN * 1,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH * 2,
                BUTTON_HEIGHT * 2);
        button_START.setForeground(Color.WHITE);
        button_START.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                if (AreaFight.screenMode.equals(EnumScreenMode.ScreenMode.GAMEOVER)) {
                    AreaFight.initMap();
                    AreaFight.initPara();
                }
                AreaFight.screenMode = EnumScreenMode.ScreenMode.INGAME;
                repaint();

            }
        });

        button_STOP = new JButton("STOP");
        this.add(button_STOP);
        button_STOP.setMargin(new Insets(0, 0, 0, 0));
        button_STOP.setBounds(SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 8 + BUTTON_HORIZONTAL_MARGIN * 1,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH * 2,
                BUTTON_HEIGHT * 2);
        button_STOP.setForeground(Color.WHITE);
        button_STOP.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {
                boolean first_push = true;
                if (AreaFight.screenMode.equals(EnumScreenMode.ScreenMode.INGAME) && first_push) {
                    AreaFight.screenMode = EnumScreenMode.ScreenMode.STOP;
                    first_push = false;
                }
                if (AreaFight.screenMode.equals(EnumScreenMode.ScreenMode.STOP) && first_push) {
                    AreaFight.screenMode = EnumScreenMode.ScreenMode.INGAME;
                    first_push = false;
                }
                repaint();
            }
        });

        button_CURRENT_RESET = new JButton("RESET");
        this.add(button_CURRENT_RESET);
        button_CURRENT_RESET.setMargin(new Insets(0, 0, 0, 0));
        button_CURRENT_RESET.setBounds(SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 10 + BUTTON_HORIZONTAL_MARGIN * 1,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 0, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_CURRENT_RESET.setForeground(Color.WHITE);
        button_CURRENT_RESET.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {

                if (dirMode.equals(EnumDirMode.DirMode.NW)) {
                    bul_NW_pw = bul_fpw_real;
                    bul_NW_sp = bul_fsp_real;
                    bul_NW_bl = bul_fbl_real;
                    bul_NW_fr = bul_ffr_real;
                    bul_NW_rn = bul_frn_real;
                    bul_NW_ax = bul_fax_real;
                }
                if (dirMode.equals(EnumDirMode.DirMode.NE)) {
                    bul_NE_pw = bul_fpw_real;
                    bul_NE_sp = bul_fsp_real;
                    bul_NE_bl = bul_fbl_real;
                    bul_NE_fr = bul_ffr_real;
                    bul_NE_rn = bul_frn_real;
                    bul_NE_ax = bul_fax_real;
                }
                if (dirMode.equals(EnumDirMode.DirMode.SW)) {
                    bul_SW_pw = bul_fpw_real;
                    bul_SW_sp = bul_fsp_real;
                    bul_SW_bl = bul_fbl_real;
                    bul_SW_fr = bul_ffr_real;
                    bul_SW_rn = bul_frn_real;
                    bul_SW_ax = bul_fax_real;
                }

                if (dirMode.equals(EnumDirMode.DirMode.SE)) {
                    bul_SE_pw = bul_fpw_real;
                    bul_SE_sp = bul_fsp_real;
                    bul_SE_bl = bul_fbl_real;
                    bul_SE_fr = bul_ffr_real;
                    bul_SE_rn = bul_frn_real;
                    bul_SE_ax = bul_fax_real;
                }
                slider_pw.setValue(BUL_FPW);
                slider_sp.setValue(BUL_FSP);
                slider_bl.setValue(BUL_FBL);
                slider_fr.setValue(BUL_FFR);
                AreaFight.initPara();

                repaint();
            }
        });

        button_ALL_RESET = new JButton("ALL RESET");
        this.add(button_ALL_RESET);
        button_ALL_RESET.setMargin(new Insets(0, 0, 0, 0));
        button_ALL_RESET.setBounds(SLIDER_LEFTMARGIN * 1 + BUTTON_WIDTH * 10 + BUTTON_HORIZONTAL_MARGIN * 1,
                button_pivot_y + BUTTON_VERTICAL_MARGIN * 1, BUTTON_WIDTH * 1,
                BUTTON_HEIGHT * 1);
        button_ALL_RESET.setForeground(Color.WHITE);
        button_ALL_RESET.addActionListener(new ActionListener() {
            // @Override
            public void actionPerformed(ActionEvent e) {

                AreaFight.map.removeAllBullet();
                color_NW = COLOR_FNW;
                color_NE = COLOR_FNE;
                color_SW = COLOR_FSW;
                color_SE = COLOR_FSE;
                initControl();
                AreaFight.init_para_from_control_field_frag = true;
                AreaFight.screenMode = EnumScreenMode.ScreenMode.TITLE;
                repaint();
            }
        });

        switch (dirMode) {
            case NW:
                button_NW.setEnabled(false);
                break;
            case NE:
                button_NE.setEnabled(false);
                break;
            case SW:
                button_SW.setEnabled(false);
                break;
            case SE:
                button_SE.setEnabled(false);
                break;
            default:
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        controlDraw(g);
    }

    public void controlDraw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, AreaFight.WIDTH, AreaFight.HEIGHT);
        g.setColor(COLOR_SLIDER);
        g.fillRoundRect(SLIDER_LEFTMARGIN / 2, SLIDER_TOPMARGIN / 2, SLIDER_WIDTH * 1 + SLIDER_LEFTMARGIN,
                SLIDER_HEIGHT * 6 + SLIDER_TOPMARGIN, SLIDER_LEFTMARGIN, SLIDER_TOPMARGIN);

        button_NW.setForeground(color_NW);
        button_NE.setForeground(color_NE);
        button_SW.setForeground(color_SW);
        button_SE.setForeground(color_SE);

        Color color_a_gray = new Color(10, 10, 10);
        Color color_c_gray = new Color(255, 255, 255);
        button_NW.setBackground(color_a_gray);
        button_NE.setBackground(color_a_gray);
        button_SW.setBackground(color_a_gray);
        button_SE.setBackground(color_a_gray);
        button_R.setBackground(color_a_gray);
        button_B.setBackground(color_a_gray);
        button_G.setBackground(color_a_gray);
        button_Y.setBackground(color_a_gray);
        button_BulNormal.setBackground(color_a_gray);
        button_BulStrong.setBackground(color_a_gray);
        button_START.setBackground(color_a_gray);
        button_STOP.setBackground(color_a_gray);
        button_CURRENT_RESET.setBackground(color_a_gray);
        button_ALL_RESET.setBackground(color_a_gray);

        button_R.setEnabled(color_button_frag && !(dirMode.equals(EnumDirMode.DirMode.NW) && color_NW.equals(Color.RED))
                && !(dirMode.equals(EnumDirMode.DirMode.NE) && color_NE.equals(Color.RED))
                && !(dirMode.equals(EnumDirMode.DirMode.SW) && color_SW.equals(Color.RED))
                && !(dirMode.equals(EnumDirMode.DirMode.SE) && color_SE.equals(Color.RED)));
        button_B.setEnabled(
                color_button_frag && !(dirMode.equals(EnumDirMode.DirMode.NW) && color_NW.equals(Color.BLUE))
                        && !(dirMode.equals(EnumDirMode.DirMode.NE) && color_NE.equals(Color.BLUE))
                        && !(dirMode.equals(EnumDirMode.DirMode.SW) && color_SW.equals(Color.BLUE))
                        && !(dirMode.equals(EnumDirMode.DirMode.SE) && color_SE.equals(Color.BLUE)));
        button_G.setEnabled(
                color_button_frag && !(dirMode.equals(EnumDirMode.DirMode.NW) && color_NW.equals(Color.GREEN))
                        && !(dirMode.equals(EnumDirMode.DirMode.NE) && color_NE.equals(Color.GREEN))
                        && !(dirMode.equals(EnumDirMode.DirMode.SW) && color_SW.equals(Color.GREEN))
                        && !(dirMode.equals(EnumDirMode.DirMode.SE) && color_SE.equals(Color.GREEN)));
        button_Y.setEnabled(
                color_button_frag && !(dirMode.equals(EnumDirMode.DirMode.NW) && color_NW.equals(Color.YELLOW))
                        && !(dirMode.equals(EnumDirMode.DirMode.NE) && color_NE.equals(Color.YELLOW))
                        && !(dirMode.equals(EnumDirMode.DirMode.SW) && color_SW.equals(Color.YELLOW))
                        && !(dirMode.equals(EnumDirMode.DirMode.SE) && color_SE.equals(Color.YELLOW)));
        button_BulNormal
                .setEnabled(color_button_frag && !(dirMode.equals(EnumDirMode.DirMode.NW) && "NORMAL".equals(bullet_NW))
                        && !(dirMode.equals(EnumDirMode.DirMode.NE) && "NORMAL".equals(bullet_NE))
                        && !(dirMode.equals(EnumDirMode.DirMode.SW) && "NORMAL".equals(bullet_SW))
                        && !(dirMode.equals(EnumDirMode.DirMode.SE) && "NORMAL".equals(bullet_SE)));
        button_BulStrong
                .setEnabled(color_button_frag && !(dirMode.equals(EnumDirMode.DirMode.NW) && "STRONG".equals(bullet_NW))
                        && !(dirMode.equals(EnumDirMode.DirMode.NE) && "STRONG".equals(bullet_NE))
                        && !(dirMode.equals(EnumDirMode.DirMode.SW) && "STRONG".equals(bullet_SW))
                        && !(dirMode.equals(EnumDirMode.DirMode.SE) && "STRONG".equals(bullet_SE)));
        button_START.setEnabled(start_button_frag);
        button_STOP.setEnabled(stop_button_frag);
        button_STOP.setText(AreaFight.screenMode.equals(EnumScreenMode.ScreenMode.STOP) ? "RESUME" : "STOP");
        button_CURRENT_RESET.setEnabled(reset_button_frag);
        button_ALL_RESET.setEnabled(reset_button_frag);
        repaint();

        switch (dirMode) {
            case NW:
                button_BulNormal.setForeground(color_NW);
                button_BulStrong.setForeground(color_NW);
                slider_pw.setForeground(color_NW);
                slider_sp.setForeground(color_NW);
                slider_bl.setForeground(color_NW);
                slider_fr.setForeground(color_NW);
                slider_rn.setForeground(color_NW);
                slider_ax.setForeground(color_NW);
                break;
            case NE:
                button_BulNormal.setForeground(color_NE);
                button_BulStrong.setForeground(color_NE);
                slider_pw.setForeground(color_NE);
                slider_sp.setForeground(color_NE);
                slider_bl.setForeground(color_NE);
                slider_fr.setForeground(color_NE);
                slider_rn.setForeground(color_NE);
                slider_ax.setForeground(color_NE);
                break;
            case SW:
                button_BulNormal.setForeground(color_SW);
                button_BulStrong.setForeground(color_SW);
                slider_pw.setForeground(color_SW);
                slider_sp.setForeground(color_SW);
                slider_bl.setForeground(color_SW);
                slider_fr.setForeground(color_SW);
                slider_rn.setForeground(color_SW);
                slider_ax.setForeground(color_SW);
                break;
            case SE:
                button_BulNormal.setForeground(color_SE);
                button_BulStrong.setForeground(color_SE);
                slider_pw.setForeground(color_SE);
                slider_sp.setForeground(color_SE);
                slider_bl.setForeground(color_SE);
                slider_fr.setForeground(color_SE);
                slider_rn.setForeground(color_SE);
                slider_ax.setForeground(color_SE);
                break;
            default:
        }
    }

    public int getValueFromSliderName(String slider_name) {
        int value = -1;

        for (int i = 0; i < slider_array.size(); i++) {
            if (slider_name.equals(slidername_array.get(i)))
                value = slider_array.get(i).getValue();
        }
        return value;
    }

    public void actionPerformed(ActionEvent e) {

    }
}
