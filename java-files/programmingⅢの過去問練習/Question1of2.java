public class Question1of2 {
	public static void main(String[] args) {
		Seiseki data = new Seiseki(100, 80, 40);
		data.show();
	}
}

class Seiseki{
	int Kokugo, Sugaku, Eigo;
	
	public Seiseki(int score1, int score2, int score3) {
		Kokugo = score1;
		Sugaku = score2;
		Eigo = score3;
	}
	
	public void show() {
		System.out.println(Kokugo);
		System.out.println(Sugaku);
		System.out.println(Eigo);
	}
}