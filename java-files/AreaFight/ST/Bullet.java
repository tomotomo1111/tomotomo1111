import java.awt.*;
import java.util.*;

public interface Bullet {

    int size = 2;
    double multiShootAngle = 0.1;

    void ability(ArrayList<Bullet> Bul);

    int getAbi();

    double getPosX();

    double getPosY();

    double getSpeed();

    double getAngle();

    int getPower();

    int getColorInt();

    Color getColor();

    void setPosX(double pos_x);

    void setPosY(double pos_y);

    void setAngle(double angle);

    void setPower(int power);

    void decSp();

    void draw(Graphics g);
}

class BulletNormal implements Bullet {

    private double pos_x;
    private double pos_y;
    private double speed;
    private double angle;
    private int power;
    private int abi_count;
    private Color color;

    public BulletNormal(double pos_x, double pos_y, double speed, double angle, int power, int abi_count, Color color) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.speed = speed;
        this.angle = angle;
        this.power = power;
        this.abi_count = abi_count;
        this.color = color;
    }

    public void ability(ArrayList<Bullet> Bul) {
        // imanotokoro mitei
    }

    public int getAbi() {
        return abi_count;
    }

    public double getPosX() {
        return pos_x;
    }

    public double getPosY() {
        return pos_y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public int getPower() {
        return power;
    }

    public int getColorInt() {
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

    public Color getColor() {
        return this.color;
    }

    public void setPosX(double pos_x) {
        this.pos_x = pos_x;
    }

    public void setPosY(double pos_y) {
        this.pos_y = pos_y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void decSp() {
        if (speed > 0)
            speed--;
    }

    public void draw(Graphics g) {

        if (this.power >= 10) {
            g.setColor(new Color(255, 255, 255, this.power * 5));
            g.drawOval((int) pos_x - size * 2, (int) pos_y - size * 2, size * 4, size * 4);
        }

        g.setColor(color);
        g.fillOval((int) pos_x - size, (int) pos_y - size, 2 * size, 2 * size);
    }
}

class BulletStrong implements Bullet {
    private double pos_x;
    private double pos_y;
    private double speed;
    private double angle;
    private int power;
    private int abi_count;
    private Color color;

    public BulletStrong(double pos_x, double pos_y, double speed, double angle, int power, int abi_count, Color color) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.speed = speed;
        this.angle = angle;
        this.power = power;
        this.abi_count = abi_count;
        this.color = color;
    }

    // nurikaeta bunndake size tiisaku
    public void ability(ArrayList<Bullet> Bul) {

        BulletStrong bullet;

        if (this.abi_count > 0) {

            int newPower = (int) (this.power / 5.0);
            newPower = newPower > 1.0 ? newPower : 1;

            double newSpeed = this.speed + 0.3;
            newSpeed = newSpeed < 1.0 ? newSpeed : 1.0;

            if (this.abi_count % 15 == 0) {

                for (int i = 0; i < 3; i++) {
                    bullet = new BulletStrong(this.pos_x, this.pos_y, newSpeed,
                            this.angle - multiShootAngle * (i - 1), newPower,
                            --abi_count,
                            color);
                    Bul.add(bullet);
                }

            } else {

                bullet = new BulletStrong(this.pos_x, this.pos_y, this.speed, this.angle,
                        this.power,
                        --abi_count,
                        color);
                Bul.add(bullet);
            }
        }
    }

    public int getAbi() {
        return abi_count;
    }

    public double getPosX() {
        return pos_x;
    }

    public double getPosY() {
        return pos_y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public int getPower() {
        return power;
    }

    public int getColorInt() {
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

    public Color getColor() {
        return this.color;
    }

    public void setPosX(double pos_x) {
        this.pos_x = pos_x;
    }

    public void setPosY(double pos_y) {
        this.pos_y = pos_y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void decSp() {
        if (speed > 0)
            speed--;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 255, 255, 41 + this.power * 4));
        g.fillOval((int) pos_x - size * 2, (int) pos_y - size * 2, size * 4, size * 4);

        g.setColor(color);
        g.fillOval((int) pos_x - size, (int) pos_y - size, 2 * size, 2 * size);
    }
}

class BulletUniverse implements Bullet {
    private double pos_x;
    private double pos_y;
    private double speed;
    private double angle;
    private int power;
    private int abi_count;
    private Color color;

    public BulletUniverse(double pos_x, double pos_y, double speed, double angle, int power, int abi_count,
            Color color) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.speed = speed;
        this.angle = angle;
        this.power = power;
        this.abi_count = abi_count;
        this.color = color;
    }

    public void ability(ArrayList<Bullet> Bul) {

    }

    public int getAbi() {
        return abi_count;
    }

    public double getPosX() {
        return pos_x;
    }

    public double getPosY() {
        return pos_y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public int getPower() {
        return power;
    }

    public int getColorInt() {
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

    public Color getColor() {
        return this.color;
    }

    public void setPosX(double pos_x) {
        this.pos_x = pos_x;
    }

    public void setPosY(double pos_y) {
        this.pos_y = pos_y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void decSp() {
        if (speed > 0)
            speed--;
    }

    public void draw(Graphics g) {
        if (this.power >= 10) {
            g.setColor(new Color(255, 255, 255, this.power * 5));
            g.drawOval((int) pos_x - size * 2, (int) pos_y - size * 2, size * 4, size * 4);
        }

        g.setColor(color);
        g.fillOval((int) pos_x - size, (int) pos_y - size, 2 * size, 2 * size);
    }
}