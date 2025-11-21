public class Question1of3 {
	public static void main(String[] args) {
		SeisekiEx data = new SeisekiEx(100, 80, 40, 50, 4);
		data.show();
	}
}

class SeisekiEx extends Seiseki{
	int Rika, Tiri;
	
	public SeisekiEx(int score1, int score2, int score3, int score4, int score5) {
		super(score1, score2, score3);
		Rika = score4;
		Tiri = score5;
	}
	
	public void show() {
		System.out.println(Kokugo);
		System.out.println(Sugaku);
		System.out.println(Eigo);
		System.out.println(Rika);
		System.out.println(Tiri);
	}
}