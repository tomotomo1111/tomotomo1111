import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AreaFight {

    public static final int WIDTH = Map.LEFTMARGIN + Map.AREA_WIDTH + Map.RIGHTMARGIN + 15;
    public static final int HEIGHT = Map.UPMARGIN + Map.AREA_HEIGHT + Map.DOWNMARGIN + 40;
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static EnumScreenMode.ScreenMode screenMode = EnumScreenMode.ScreenMode.TITLE;

    static Map map = new Map();
    static Control control = new Control();

    static boolean test_frag = false; // test you
    static boolean init_para_from_control_field_frag = true;

    public static void main(String[] args) {

        MyFrame myFrame = new MyFrame();
        initMap();
        myFrame.getContentPane().add(map);

        MyFrame controlFrame = new MyFrame();

        control.setLayout(null);
        control.initControl();
        controlFrame.setBounds((int) (SCREEN_WIDTH * 0.6), (int) (SCREEN_HEIGHT * 0.2), WIDTH, HEIGHT);
        controlFrame.getContentPane().add(control);

        Random rand = new Random();
        int time_count = 0;

        while (true) {

            myFrame.repaint();
            controlFrame.repaint();
            updateParaFromSlider(control);

            if (screenMode.equals(EnumScreenMode.ScreenMode.TITLE)) {

                initMap();
                Control.start_button_frag = true;
                Control.color_button_frag = true;
                Control.stop_button_frag = false;
                Control.reset_button_frag = true;
            }

            if (screenMode.equals(EnumScreenMode.ScreenMode.INGAME)) {

                if (init_para_from_control_field_frag) {
                    initPara();
                    init_para_from_control_field_frag = false;
                }

                Control.start_button_frag = false;
                Control.color_button_frag = false;
                Control.stop_button_frag = true;
                Control.reset_button_frag = false;

                if (time_count % Map.canon_NW.getBulFirerate() == 0 && !test_frag)
                    map.makeBullet_NW(rand.nextInt(Map.canon_NW.getBulPower()),
                            Map.canon_NW.getPosX() + Map.canon_NW.getsize(),
                            Map.canon_NW.getPosY() + Map.canon_NW.getsize(), Control.color_NW,
                            Map.canon_NW.getBulBlur(), Control.bullet_NW);
                if (time_count % Map.canon_NE.getBulFirerate() == 0 && !test_frag)
                    map.makeBullet_NE(rand.nextInt(Map.canon_NE.getBulPower()), Map.canon_NE.getPosX(),
                            Map.canon_NE.getPosY() + Map.canon_NE.getsize(), Control.color_NE,
                            Map.canon_NE.getBulBlur(), Control.bullet_NE);
                if (time_count % Map.canon_SW.getBulFirerate() == 0 && !test_frag)
                    map.makeBullet_SW(rand.nextInt(Map.canon_SW.getBulPower()),
                            Map.canon_SW.getPosX() + Map.canon_SW.getsize(),
                            Map.canon_SW.getPosY(), Control.color_SW, Map.canon_SW.getBulBlur(), Control.bullet_SW);
                if (time_count % Map.canon_SE.getBulFirerate() == 0 && !test_frag)
                    map.makeBullet_SE(rand.nextInt(Map.canon_SE.getBulPower()), Map.canon_SE.getPosX(),
                            Map.canon_SE.getPosY(), Control.color_SE, Map.canon_SE.getBulBlur(), Control.bullet_SE);

                if (time_count % Map.canon_NW.getBulFirerate() == 0 && test_frag)
                    map.makeBullet_NW(rand.nextInt(Map.canon_NW.getBulPower()),
                            Map.canon_NW.getPosX() + Map.canon_NW.getsize(),
                            Map.canon_NW.getPosY() + Map.canon_NW.getsize(), Control.color_NW,
                            Map.canon_NW.getBulBlur(), "");
                if (time_count % Map.canon_NE.getBulFirerate() == 0 && test_frag)
                    map.makeBullet_NE(rand.nextInt(Map.canon_NE.getBulPower()), Map.canon_NE.getPosX(),
                            Map.canon_NE.getPosY() + Map.canon_NE.getsize(), Control.color_NE,
                            Map.canon_NE.getBulBlur(), "");
                if (time_count % Map.canon_SW.getBulFirerate() == 0 && test_frag)
                    map.makeBullet_SW(rand.nextInt(Map.canon_SW.getBulPower()),
                            Map.canon_SW.getPosX() + Map.canon_SW.getsize(),
                            Map.canon_SW.getPosY(), Control.color_SW, Map.canon_SW.getBulBlur(), "");
                if (time_count % Map.canon_SE.getBulFirerate() == 0 && test_frag) // test ji nomi
                    map.makeBullet_SE(rand.nextInt(Map.canon_SE.getBulPower()), Map.canon_SE.getPosX(),
                            Map.canon_SE.getPosY(), Control.color_SE, Map.canon_SE.getBulBlur(), "STRONG");

                map.moveBullet();
                map.checkBulletCollision();
                map.lotateAllCanon();
                map.checkAllBulletCollisionToColor();
                map.removeAllBulletAboveCanonPower();

                if (map.checkWin()) {
                    screenMode = EnumScreenMode.ScreenMode.GAMEOVER;
                    map.removeAllBullet();
                }
            }

            if (screenMode.equals(EnumScreenMode.ScreenMode.GAMEOVER)) {

                initMap();
                Control.start_button_frag = true;
                Control.color_button_frag = true;
                Control.stop_button_frag = false;
                Control.reset_button_frag = true;
            }

            if (screenMode.equals(EnumScreenMode.ScreenMode.STOP)) {

                Control.start_button_frag = false;
                Control.color_button_frag = false;
                Control.stop_button_frag = true;
                Control.reset_button_frag = true;
            }

            try {
                Thread.sleep(5);
            } catch (Exception e) {
                System.exit(-1);
            }

            time_count++;
        }
    }

    public static void initMap() {
        map.initMap(Control.color_NW, Control.color_NE, Control.color_SW, Control.color_SE);
    }

    public static void initPara() {
        
        Map.canon_NW.setBulPower((int) Control.bul_NW_pw);
        Map.canon_NE.setBulPower((int) Control.bul_NE_pw);
        Map.canon_SW.setBulPower((int) Control.bul_SW_pw);
        Map.canon_SE.setBulPower((int) Control.bul_SE_pw);
        Map.canon_NW.setBulSpeed(Control.bul_NW_sp);
        Map.canon_NE.setBulSpeed(Control.bul_NE_sp);
        Map.canon_SW.setBulSpeed(Control.bul_SW_sp);
        Map.canon_SE.setBulSpeed(Control.bul_SE_sp);
        Map.canon_NW.setBulBlur((int) Control.bul_NW_bl);
        Map.canon_NE.setBulBlur((int) Control.bul_NE_bl);
        Map.canon_SW.setBulBlur((int) Control.bul_SW_bl);
        Map.canon_SE.setBulBlur((int) Control.bul_SE_bl);
        Map.canon_NW.setBulFirerate((int) Control.bul_NW_fr);
        Map.canon_NE.setBulFirerate((int) Control.bul_NE_fr);
        Map.canon_SW.setBulFirerate((int) Control.bul_SW_fr);
        Map.canon_SE.setBulFirerate((int) Control.bul_SE_fr);
        Map.canon_NW.setAngleRange(Control.bul_NW_rn);
        Map.canon_NE.setAngleRange(Control.bul_NE_rn);
        Map.canon_SW.setAngleRange(Control.bul_SW_rn);
        Map.canon_SE.setAngleRange(Control.bul_SE_rn);
        Map.canon_NW.setAxisOffset(Control.bul_NW_ax);
        Map.canon_NE.setAxisOffset(Control.bul_NE_ax);
        Map.canon_SW.setAxisOffset(Control.bul_SW_ax);
        Map.canon_SE.setAxisOffset(Control.bul_SE_ax);
    }

    private static void updateParaFromSlider(Control control) {
        EnumDirMode.DirMode f_DirMode = Control.dirMode;

        int newPower = (int) (control.getValueFromSliderName("slider_pw") / 2 + 1);
        double newSpeed = (control.getValueFromSliderName("slider_sp") / 100.0);
        int newBlur = (int) (control.getValueFromSliderName("slider_bl") + 1);
        int newFirerate = (101 - control.getValueFromSliderName("slider_fr"));
        double newAngleRange = Math.PI * control.getValueFromSliderName("slider_rn") / 100;
        double newAxisOffset = (Math.PI * 0.26) * (control.getValueFromSliderName("slider_ax") - 50) / 50.0;


        switch (Control.dirMode) {
            case NW:
                if (f_DirMode.equals(EnumDirMode.DirMode.NW)) {
                    Map.canon_NW.setBulPower(newPower);
                    Map.canon_NW.setBulSpeed(newSpeed);
                    Map.canon_NW.setBulBlur(newBlur);
                    Map.canon_NW.setBulFirerate(newFirerate);
                    Map.canon_NW.setAngleRange(newAngleRange);
                    Map.canon_NW.setAxisOffset(newAxisOffset);
                    Control.bul_NW_pw = newPower;
                    Control.bul_NW_sp = newSpeed;
                    Control.bul_NW_bl = newBlur;
                    Control.bul_NW_fr = newFirerate;
                    Control.bul_NW_rn = newAngleRange;
                    Control.bul_NW_ax = newAxisOffset;
                }
                break;
            case NE:
                if (f_DirMode.equals(EnumDirMode.DirMode.NE)) {
                    Map.canon_NE.setBulPower(newPower);
                    Map.canon_NE.setBulSpeed(newSpeed);
                    Map.canon_NE.setBulBlur(newBlur);
                    Map.canon_NE.setBulFirerate(newFirerate);
                    Map.canon_NE.setAngleRange(newAngleRange);
                    Map.canon_NE.setAxisOffset(newAxisOffset);
                    Control.bul_NE_pw = newPower;
                    Control.bul_NE_sp = newSpeed;
                    Control.bul_NE_bl = newBlur;
                    Control.bul_NE_fr = newFirerate;
                    Control.bul_NE_rn = newAngleRange;
                    Control.bul_NE_ax = newAxisOffset;
                }
                break;
            case SW:
                if (f_DirMode.equals(EnumDirMode.DirMode.SW)) {
                    Map.canon_SW.setBulPower(newPower);
                    Map.canon_SW.setBulSpeed(newSpeed);
                    Map.canon_SW.setBulBlur(newBlur);
                    Map.canon_SW.setBulFirerate(newFirerate);
                    Map.canon_SW.setAngleRange(newAngleRange);
                    Map.canon_SW.setAxisOffset(newAxisOffset);
                    Control.bul_SW_pw = newPower;
                    Control.bul_SW_sp = newSpeed;
                    Control.bul_SW_bl = newBlur;
                    Control.bul_SW_fr = newFirerate;
                    Control.bul_SW_rn = newAngleRange;
                    Control.bul_SW_ax = newAxisOffset;
                }
                break;
            case SE:
                if (f_DirMode.equals(EnumDirMode.DirMode.SE)) {
                    Map.canon_SE.setBulPower(newPower);
                    Map.canon_SE.setBulSpeed(newSpeed);
                    Map.canon_SE.setBulBlur(newBlur);
                    Map.canon_SE.setBulFirerate(newFirerate);
                    Map.canon_SE.setAngleRange(newAngleRange);
                    Map.canon_SE.setAxisOffset(newAxisOffset);
                    Control.bul_SE_pw = newPower;
                    Control.bul_SE_sp = newSpeed;
                    Control.bul_SE_bl = newBlur;
                    Control.bul_SE_fr = newFirerate;
                    Control.bul_SE_rn = newAngleRange;
                    Control.bul_SE_ax = newAxisOffset;
                }
                break;
            default:
        }
    }

    public static void timerAction(String name, double time) {
        if (name.equals("updateParaFromSlider")) {

        }
    }
}
