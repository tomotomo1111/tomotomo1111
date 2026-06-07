import javax.swing.*;
import java.awt.*;

public class Canon {

    private double pos_x;
    private double pos_y;
    private final int size = 20;
    private double angle;
    private double firstAngle;

    public Canon(double pos_x, double pos_y, double angle) {

        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.angle = angle;
        firstAngle = angle;
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

    public double getFirstAngle() {
        return firstAngle;
    }

    public int getSize() {
        return size;
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

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        // g.drawOval((int) ((pos_x + size) * Math.cos(angle - Math.PI / 4) - (pos_y +
        // size) * Math.sin(angle - Math.PI / 4)),
        // (int) ((pos_x + size) * Math.sin(angle - Math.PI / 4) + (pos_y + size) *
        // Math.cos(angle - Math.PI / 4)), size, size);

        g.fillOval((int)pos_x, (int)pos_y, size, size);
    }
}
