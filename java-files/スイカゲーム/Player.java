import java.awt.*;

public class Player {
    private Vector2 pos;
    private double angle;
    private double v;
    private Vector2 v_vec;
    private int size;
    private double friction;

    public Player(Vector2 pos, double angle, double v, double v_x, double v_y, int size, double friction) {
        this.pos = pos;
        this.angle = angle;
        this.v = v;
        this.v_vec = new Vector2(v_x, v_y);
        this.size = size;
        this.friction = friction;
    }

    public void move() {
        this.pos.x += this.v_vec.x;
        this.pos.y += this.v_vec.y;
        friction();
    }

    private void friction() {
        this.v_vec.mul(this.friction);
        v *= this.friction;
    }

    private void calNewVec() {
        v_vec.x = v * Math.cos(angle);
        v_vec.y = v * Math.sin(angle);
    }

    public void paintPlayer(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int) this.pos.x, (int) this.pos.y, size, size);
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getPos() {
        return this.pos;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        calNewVec();
    }

    public double getAngle() {
        return this.angle;
    }

    public void setVelocity(double v) {
        this.v = v;
        calNewVec();
    }

    public double getVelocity() {
        return this.v;
    }

    public Vector2 getVec() {
        return this.v_vec;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setFriction() {
        this.friction = friction;
    }

    public double getFriction() {
        return this.friction;
    }
}
