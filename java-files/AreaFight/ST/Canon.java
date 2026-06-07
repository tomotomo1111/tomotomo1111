import java.awt.*;
import javax.swing.*;

public class Canon{
    
    private int size = 20;
    private double pos_x;
    private double pos_y;
    private double angle;
    private double angle_range = Math.PI * 0.8;
    private double firstAngle;
    private double axis_offset = 0;
    private double hp;
    private int bul_power = 10;
    private double bul_speed = 0.5;
    private int bul_blur = 50;
    private int bul_firerate = 5; 
    private Color color;
    private boolean isBroken = false;

    Font font_hp = new Font("Serif", Font.BOLD, 15);
    FontMetrics metr_hp;

    public Canon(double pos_x, double pos_y, double angle, int size, double hp, Color color) {

        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.angle = angle;
        firstAngle = angle;
        this.size = size;
        this.hp = hp;
        this.color = color;
    }

    public int getsize() {
        return size;
    }

    public double getPosX() {
        return pos_x;
    }

    public double getPosY() {
        return pos_y;
    }

    public double getAngle() {
        return angle;
    }

    public double getAngleRange() {
        return angle_range;
    }

    public double getFirstAngle() {
        return firstAngle;
    }

    public double getAxisOffset() {
        return axis_offset;
    }

    public double getHp() {
        return hp;
    }

    public boolean getIsBroken() {
        return isBroken;
    }

    public Color getColor() {
        return color;
    }

    public double getBulSpeed() {
        return bul_speed;
    }

    public int getBulPower() {
        return bul_power;
    }

    public int getBulBlur() {
        return bul_blur;
    }

    public int getBulFirerate() {
        return bul_firerate;
    }

    public void setPosX(double newPos_x) {
        this.pos_x = newPos_x;
    }

    public void setPosY(double newPos_y) {
        this.pos_y = newPos_y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setAngleRange(double angle_range) {
        this.angle_range = angle_range;
    }

    public void setAxisOffset(double axis_offset) {
        this.axis_offset = axis_offset;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
        if(isBroken) this.hp = 0;
    }

    public void setBulSpeed(double bul_speed) {
        this.bul_speed = bul_speed;
    }

    public void setBulPower(int bul_power) {
        this.bul_power = bul_power;
    }

    public void setBulBlur(int bul_blur) {
        this.bul_blur = bul_blur;
    }

    public void setBulFirerate(int bul_firerate) {
        this.bul_firerate = bul_firerate;
    }

    public void damage(double damage) {
        if(!this.isBroken) this.hp -= damage;
    }

    public boolean checkBroken() {
        if(this.hp <= 0) return true;
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        /*
        g.drawOval((int) ((pos_x + size) * Math.cos(angle - Math.PI / 4) - (pos_y +
        size) * Math.sin(angle - Math.PI / 4)),
        (int) ((pos_x + size) * Math.sin(angle - Math.PI / 4) + (pos_y + size) *
        Math.cos(angle - Math.PI / 4)), size, size);
        */
        g.fillOval((int)pos_x, (int)pos_y, size, size);

        g.setColor(Color.WHITE);
        g.setFont(font_hp);
        metr_hp = g.getFontMetrics(font_hp);
        String str_hp = String.valueOf(this.hp);
        int width = metr_hp.stringWidth(str_hp);
        g.drawString(str_hp, (int)pos_x - (width - size) / 2, firstAngle >= Math.PI ? (int)pos_y : (int)(pos_y + size * 1.5));
    }
}