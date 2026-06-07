import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map extends JPanel {

    private static final int[][] area = new int[200][200];
    private static final int DOT_W = 2;
    private static final int DOT_H = 2;
    public static final int AREA_WIDTH = area[0].length * DOT_W;
    public static final int AREA_HEIGHT = area.length * DOT_H;
    public static final int LEFTMARGIN = 10;
    public static final int RIGHTMARGIN = 10;
    public static final int UPMARGIN = 10;
    public static final int DOWNMARGIN = 10;
    private static final int EDGE = 1;
    public static final int MAP_EDGE = 5;
    private static final int CANON_SIZE = 20;
    private static final double CANON_HP = 100;

    static Canon canon_NW, canon_NE, canon_SW, canon_SE;

    private static int numOf360Scale = 0;
    private static double theta = 0;
    private static final int MULTI_COUNT_MAX = 30;

    public static ArrayList<Bullet> Bul_NW = new ArrayList<>();
    public static ArrayList<Bullet> Bul_NE = new ArrayList<>();
    public static ArrayList<Bullet> Bul_SW = new ArrayList<>();
    public static ArrayList<Bullet> Bul_SE = new ArrayList<>();
    

    public void initMap(Color nw, Color ne, Color sw, Color se) {

        removeAll();

        canon_NW = new Canon(LEFTMARGIN + CANON_SIZE, UPMARGIN + CANON_SIZE, Math.PI * 7 / 4, CANON_SIZE, CANON_HP, nw);
        canon_NE = new Canon(LEFTMARGIN + AREA_WIDTH - CANON_SIZE * 2, UPMARGIN + CANON_SIZE, Math.PI * 5 / 4,
                CANON_SIZE, CANON_HP, ne);
        canon_SW = new Canon(LEFTMARGIN + CANON_SIZE, UPMARGIN + AREA_HEIGHT - CANON_SIZE * 2, Math.PI * 1 / 4,
                CANON_SIZE, CANON_HP, sw);
        canon_SE = new Canon(LEFTMARGIN + AREA_WIDTH - CANON_SIZE * 2, UPMARGIN + AREA_HEIGHT - CANON_SIZE * 2,
                Math.PI * 3 / 4, CANON_SIZE,
                CANON_HP, se);

        int blocksize_X = (int) (area.length / 2);
        int blocksize_Y = (int) (area[0].length / 2);

        for (int i = 0; i < area.length; i++) {

            for (int j = 0; j < area[0].length; j++) {

                if (i >= blocksize_Y && j >= blocksize_X)
                    setColor(i, j, colorToInt(se));
                if (i >= blocksize_Y && j < blocksize_X)
                    setColor(i, j, colorToInt(sw));
                if (i < blocksize_Y && j >= blocksize_X)
                    setColor(i, j, colorToInt(ne));
                if (i < blocksize_Y && j < blocksize_X)
                    setColor(i, j, colorToInt(nw));
            }
        }
    }

    public void removeAllBullet() {
        removeBullet(Bul_NW);
        removeBullet(Bul_NE);
        removeBullet(Bul_SW);
        removeBullet(Bul_SE);
    }

    private void removeBullet(ArrayList<Bullet> Bul) {
        Bul.removeAll(Bul);
    }

    private int colorToInt(Color color) {
        int c = -1;
        if (color.equals(Color.RED))
            c = 3;
        if (color.equals(Color.BLUE))
            c = 2;
        if (color.equals(Color.YELLOW))
            c = 1;
        if (color.equals(Color.GREEN))
            c = 0;
        return c;
    }

    public int getColor(int row, int col) {

        return area[row][col];
    }

    public void setColor(int row, int col, int color) {

        area[row][col] = color;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        mapDraw(g);
        canon_NE.draw(g);
        canon_NW.draw(g);
        canon_SE.draw(g);
        canon_SW.draw(g);

        bulletDraw(g, Bul_NW);
        bulletDraw(g, Bul_NE);
        bulletDraw(g, Bul_SW);
        bulletDraw(g, Bul_SE);

    }

    public void mapDraw(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, AreaFight.WIDTH, AreaFight.HEIGHT);

        for (int i = 0; i < area.length; i++) {

            for (int j = 0; j < area[0].length; j++) {

                g.setColor(Color.WHITE);

                if (i >= 0 + MAP_EDGE && i < area.length - MAP_EDGE && j >= 0 + MAP_EDGE
                        && j < area[0].length - MAP_EDGE) {
                    switch (area[i][j]) {
                        case 0:
                            if (area[i + EDGE][j] != 0 || area[i - EDGE][j] != 0 || area[i][j + EDGE] != 0
                                    || area[i][j - EDGE] != 0) {
                                g.setColor(Color.GREEN);
                            } else {
                                g.setColor(new Color(127, 255, 127, 0));
                            }
                            break;
                        case 1:
                            if (area[i + EDGE][j] != 1 || area[i - EDGE][j] != 1 || area[i][j + EDGE] != 1
                                    || area[i][j - EDGE] != 1) {
                                g.setColor(Color.YELLOW);
                            } else {
                                g.setColor(new Color(255, 255, 208, 0));
                            }
                            break;
                        case 2:
                            if (area[i + EDGE][j] != 2 || area[i - EDGE][j] != 2 || area[i][j + EDGE] != 2
                                    || area[i][j - EDGE] != 2) {
                                g.setColor(Color.BLUE);
                            } else {
                                g.setColor(new Color(127, 127, 255, 0));
                            }
                            break;
                        case 3:
                            if (area[i + EDGE][j] != 3 || area[i - EDGE][j] != 3 || area[i][j + EDGE] != 3
                                    || area[i][j - EDGE] != 3) {
                                g.setColor(Color.RED);
                            } else {
                                g.setColor(new Color(255, 127, 127, 0));
                            }
                            break;
                        default:
                            g.setColor(Color.BLACK);
                            break;
                    }
                } else {
                    g.setColor(Color.GRAY);
                }

                g.fillRect(LEFTMARGIN + j * DOT_W, UPMARGIN + i * DOT_H, DOT_W, DOT_H);
            }
        }
        repaint();
    }

    private void bulletDraw(Graphics g, ArrayList<Bullet> Bul) {

        Bullet bullet;
        for (int i = 0; i < Bul.size(); i++) {

            bullet = Bul.get(i);
            bullet.draw(g);
        }
    }

    public void makeBullet_NW(int power, double x, double y, Color color, int blur_NW, String bulName) {
        Random rand = new Random();
        double blur = (double) (rand.nextInt(blur_NW) - blur_NW / 2) / 100;

        Bullet bullet;
        if (bulName.equals("STRONG")) {
            bullet = new BulletStrong(x, y, canon_NW.getBulSpeed(), canon_NW.getAngle() + blur, power, MULTI_COUNT_MAX,
                    color);
        } else if (bulName.equals("UNIVERSE")) {
            bullet = new BulletUniverse(x, y, canon_NW.getBulSpeed(), canon_NW.getAngle() + blur, power, 10,
                    color);
        } else {
            bullet = new BulletNormal(x, y, canon_NW.getBulSpeed(), canon_NW.getAngle() + blur, power, 0,
                    color);
        }

        if (!canon_NW.getIsBroken())
            Bul_NW.add(bullet);
    }

    public void makeBullet_NE(int power, double x, double y, Color color, int blur_NE, String bulName) {
        Random rand = new Random();
        double blur = (double) (rand.nextInt(blur_NE) - blur_NE / 2) / 100;

        Bullet bullet;
        if (bulName.equals("STRONG")) {
            bullet = new BulletStrong(x, y, canon_NE.getBulSpeed(), canon_NE.getAngle() + blur, power, MULTI_COUNT_MAX,
                    color);
        } else if (bulName.equals("UNIVERSE")) {
            bullet = new BulletUniverse(x, y, canon_NE.getBulSpeed(), canon_NE.getAngle() + blur, power, 10,
                    color);
        } else {
            bullet = new BulletNormal(x, y, canon_NE.getBulSpeed(), canon_NE.getAngle() + blur, power, 0,
                    color);
        }

        if (!canon_NE.checkBroken())
            Bul_NE.add(bullet);
    }

    public void makeBullet_SW(int power, double x, double y, Color color, int blur_SW, String bulName) {
        Random rand = new Random();
        double blur = (double) (rand.nextInt(blur_SW) - blur_SW / 2) / 100;

        Bullet bullet;
        if (bulName.equals("STRONG")) {
            bullet = new BulletStrong(x, y, canon_SW.getBulSpeed(), canon_SW.getAngle() + blur, power, MULTI_COUNT_MAX,
                    color);
        } else if (bulName.equals("UNIVERSE")) {
            bullet = new BulletUniverse(x, y, canon_SW.getBulSpeed(), canon_SW.getAngle() + blur, power, 10,
                    color);
        } else {
            bullet = new BulletNormal(x, y, canon_SW.getBulSpeed(), canon_SW.getAngle() + blur, power, 0,
                    color);
        }

        if (!canon_SW.checkBroken())
            Bul_SW.add(bullet);
    }

    public void makeBullet_SE(int power, double x, double y, Color color, int blur_SE, String bulName) {
        Random rand = new Random();
        double blur = (double) (rand.nextInt(blur_SE) - blur_SE / 2) / 100;

        Bullet bullet;
        if (bulName.equals("STRONG")) {
            bullet = new BulletStrong(x, y, canon_SE.getBulSpeed(), canon_SE.getAngle() + blur, power, MULTI_COUNT_MAX,
                    color);
        } else if (bulName.equals("UNIVERSE")) {
            bullet = new BulletUniverse(x, y, canon_SE.getBulSpeed(), canon_SE.getAngle() + blur, power, 10,
                    color);
        } else {
            bullet = new BulletNormal(x, y, canon_SE.getBulSpeed(), canon_SE.getAngle() + blur, power, 0,
                    color);
        }

        if (!canon_SE.checkBroken())
            Bul_SE.add(bullet);
    }

    public void moveBullet() {

        moveAllBullet(Bul_NW);
        moveAllBullet(Bul_NE);
        moveAllBullet(Bul_SW);
        moveAllBullet(Bul_SE);
    }

    private void moveAllBullet(ArrayList<Bullet> Bul) {
        Bullet bullet;

        for (int i = 0; i < Bul.size(); i++) {
            bullet = Bul.get(i);
            double newX = bullet.getPosX() + bullet.getSpeed() * Math.cos(bullet.getAngle());
            double newY = bullet.getPosY() + bullet.getSpeed() * Math.sin(bullet.getAngle() + Math.PI);
            bullet.setPosX(newX);
            bullet.setPosY(newY);
        }
    }

    public void checkBulletCollision() {

        checkAllBulletCollisionToHome(Bul_NW);
        checkAllBulletCollisionToHome(Bul_NE);
        checkAllBulletCollisionToHome(Bul_SW);
        checkAllBulletCollisionToHome(Bul_SE);

        checkAllBulletCollisionToWall(Bul_NW);
        checkAllBulletCollisionToWall(Bul_NE);
        checkAllBulletCollisionToWall(Bul_SW);
        checkAllBulletCollisionToWall(Bul_SE);
    }

    private void checkAllBulletCollisionToHome(ArrayList<Bullet> Bul) {
        Bullet bullet;

        for (int i = 0; i < Bul.size(); i++) {
            bullet = Bul.get(i);
            if (checkBulletCollisionToHome(bullet, canon_NW)) {
                Bul.remove(i);
                canon_NW.damage(bullet.getPower());
                if (canon_NW.checkBroken())
                    canon_NW.setIsBroken(true);
            }
            if (checkBulletCollisionToHome(bullet, canon_NE)) {
                Bul.remove(i);
                canon_NE.damage(bullet.getPower());
                if (canon_NE.checkBroken())
                    canon_NE.setIsBroken(true);
            }
            if (checkBulletCollisionToHome(bullet, canon_SW)) {
                Bul.remove(i);
                canon_SW.damage(bullet.getPower());
                if (canon_SW.checkBroken())
                    canon_SW.setIsBroken(true);
            }
            if (checkBulletCollisionToHome(bullet, canon_SE)) {
                Bul.remove(i);
                canon_SE.damage(bullet.getPower());
                if (canon_SE.checkBroken())
                    canon_SE.setIsBroken(true);
            }
        }
    }

    private boolean checkBulletCollisionToHome(Bullet bullet, Canon canon) {
        if (!bullet.getColor().equals(canon.getColor())) {
            for (int i = 0; i < canon.getsize(); i++) {
                for (int j = 0; j < canon.getsize(); j++) {
                    if ((int) canon.getPosX() + i == (int) bullet.getPosX()
                            && (int) canon.getPosY() + j == (int) bullet.getPosY())
                        return true;
                }
            }
        }
        return false;
    }

    private void checkAllBulletCollisionToWall(ArrayList<Bullet> Bul) {

        Bullet bullet;
        for (int i = 0; i < Bul.size(); i++) {
            bullet = Bul.get(i);

            if (bullet.getPosX() <= LEFTMARGIN + MAP_EDGE * DOT_W) {
                bullet.setAngle(bullet.getAngle() >= Math.PI ? bullet.getAngle() + calDst(1.5, bullet)
                        : bullet.getAngle() + calDst(0.5, bullet));
            }

            if (bullet.getPosX() >= AREA_WIDTH - MAP_EDGE * DOT_W + LEFTMARGIN) {
                bullet.setAngle(bullet.getAngle() >= Math.PI ? bullet.getAngle() + calDst(1.5, bullet)
                        : bullet.getAngle() + calDst(0.5, bullet));
            }
            if (bullet.getPosY() <= UPMARGIN + MAP_EDGE * DOT_H) {
                bullet.setAngle(isAngleLeft(bullet) ? bullet.getAngle() + calDst(1.0, bullet)
                        : bullet.getAngle() + calDst(0, bullet) + Math.PI * 2);
            }
            if (bullet.getPosY() >= AREA_HEIGHT - MAP_EDGE * DOT_H + UPMARGIN) {
                bullet.setAngle(isAngleLeft(bullet) ? bullet.getAngle() + calDst(1.0, bullet)
                        : bullet.getAngle() + calDst(2, bullet) - Math.PI * 2);
            }
        }
    }

    private double calDst(double scale, Bullet bullet) {
        return (Math.PI * scale - bullet.getAngle()) * 2;
    }

    private boolean isAngleLeft(Bullet bullet) {
        if (bullet.getAngle() >= Math.PI * 0.5 && bullet.getAngle() <= Math.PI * 1.5)
            return true;
        return false;
    }

    public void lotateAllCanon() {

        numOf360Scale = (numOf360Scale + 1) % 360;
        theta = Math.PI * numOf360Scale / 180;
        
        canon_NW.setAngle(Math.sin(theta) * canon_NW.getAngleRange() + canon_NW.getFirstAngle() - canon_NW.getAxisOffset());
        canon_NE.setAngle(Math.sin(theta) * canon_NE.getAngleRange() + canon_NE.getFirstAngle() - canon_NE.getAxisOffset());
        canon_SW.setAngle(Math.sin(theta) * canon_SW.getAngleRange() + canon_SW.getFirstAngle() - canon_SW.getAxisOffset());
        canon_SE.setAngle(Math.sin(theta) * canon_SE.getAngleRange() + canon_SE.getFirstAngle() - canon_SE.getAxisOffset());
    }

    public void removeAllBulletAboveCanonPower() {

        removeBulletAboveCanonPower(Bul_NW, canon_NW.getBulPower());
        removeBulletAboveCanonPower(Bul_NE, canon_NE.getBulPower());
        removeBulletAboveCanonPower(Bul_SW, canon_SW.getBulPower());
        removeBulletAboveCanonPower(Bul_SE, canon_SE.getBulPower());
    }

    public void removeBulletAboveCanonPower(ArrayList<Bullet> Bul, int canon_pw) {

        Bullet bullet;
        int bul_p;

        for (int i = 0; i < Bul.size(); i++) {
            bullet = Bul.get(i);
            bul_p = bullet.getPower();
            if (bul_p > canon_pw)
                Bul.remove(i);
        }
    }

    public void checkAllBulletCollisionToColor() {

        checkBulletCollisionToColor(Bul_NW);
        checkBulletCollisionToColor(Bul_NE);
        checkBulletCollisionToColor(Bul_SW);
        checkBulletCollisionToColor(Bul_SE);
    }

    private void checkBulletCollisionToColor(ArrayList<Bullet> Bul) {

        Bullet bullet;
        double bul_x, bul_y;
        int bul_p;
        int bul_c;

        for (int b = 0; b < Bul.size(); b++) {
            bullet = Bul.get(b);
            bul_x = bullet.getPosX();
            bul_y = bullet.getPosY();
            bul_p = bullet.getPower();
            bul_c = bullet.getColorInt();

            if (getColor((int) ((bul_y - UPMARGIN) / DOT_H),
                    (int) ((bul_x - LEFTMARGIN) / DOT_W)) != bul_c) {
                setColor((int) ((bul_y - UPMARGIN) / DOT_H), (int) ((bul_x - LEFTMARGIN) / DOT_W),
                        bul_c);

                int X_LE, X_RI, Y_UP, Y_DN;

                X_LE = -bul_p;
                X_RI = bul_p;
                Y_UP = -bul_p;
                Y_DN = bul_p;

                if (bul_x - LEFTMARGIN - bul_p * DOT_W < 0)
                    X_LE = -(int) ((bul_x - LEFTMARGIN) / DOT_W);
                if (bul_x - LEFTMARGIN + bul_p * DOT_W >= AREA_WIDTH)
                    X_RI = area[0].length - (int) ((bul_x - LEFTMARGIN) / DOT_W);
                if (bul_y - UPMARGIN - bul_p * DOT_H < 0)
                    Y_UP = -(int) ((bul_y - UPMARGIN) / DOT_H);
                if (bul_y - UPMARGIN + bul_p * DOT_H >= AREA_HEIGHT)
                    Y_DN = area.length - (int) ((bul_y - UPMARGIN) / DOT_H);

                for (int i = X_LE; i < X_RI; i++) {

                    for (int j = Y_UP; j < Y_DN; j++) {

                        try {
                            if ((i * i + j * j) <= (bul_p * bul_p)) {
                                setColor((int) ((bul_y - UPMARGIN) / DOT_H) + j,
                                        (int) ((bul_x - LEFTMARGIN) / DOT_W) + i, bul_c);
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println("i " + i + " :j " + j + " :bul_x " + bul_x + " :bul_y " + bul_y
                                    + " :bul_p " + bul_p + " :X_LE " + X_LE + " :Y_UP " + Y_UP + " :X_RI " + X_RI
                                    + " :Y_DN " + Y_DN);
                            System.exit(-1);
                        } finally {

                        }
                    }
                }
                bullet.ability(Bul);
                Bul.remove(b);
            }
        }
    }

    public boolean checkWin() {
        Canon[] allCanon = { canon_NW, canon_NE, canon_SW, canon_SE };
        ArrayList<Canon> leftArray = new ArrayList<>();
        boolean isAllSameColor = true;

        for (int i = 0; i < allCanon.length; i++) {
            if (allCanon[i].getHp() > 0)
                leftArray.add(allCanon[i]);
        }

        for (int i = 0; i < leftArray.size() - 1; i++) {
            if (leftArray.size() == 1)
                break;
            if (!leftArray.get(i).getColor().equals(leftArray.get(i + 1).getColor()))
                isAllSameColor = false;
        }

        return isAllSameColor;
    }
}