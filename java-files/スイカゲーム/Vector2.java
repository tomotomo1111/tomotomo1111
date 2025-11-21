public class Vector2 {
    public double x, y;
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double a) {
        this.x += a;
        this.y += a;
    }

    public void add(Vector2 a) {
        this.x += a.x;
        this.y += a.y;
    }

    public void dec(double a) {
        this.x -= a;
        this.y -= a;
    }

    public void dec(Vector2 a) {
        this.x -= a.x;
        this.y -= a.y;
    }

    public void mul(double a) {
        this.x *= a;
        this.y *= a;
    }

    public void mul(Vector2 a) {
        this.x *= a.x;
        this.y *= a.y;
    }

    public void div(double a) {
        this.x /= a;
        this.y /= a;
    }

    public void div(Vector2 a) {
        this.x /= a.x;
        this.y /= a.y;
    }
}