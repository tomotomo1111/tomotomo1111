public class Hexagon {
	public void draw() {
	  Turtle t = new Turtle();
	  t.move(120,300);
      t.penDown();
      for (int i = 0; i < 6; i++) {
		t.go(75);
		t.rotate(120);
		t.go(225);
		t.rotate(120);
		t.go(75);
		t.rotate(60);
	}
}
	
	public static void main(String [] args) {
		Hexagon hex = new Hexagon();
		hex.draw();
	}
}