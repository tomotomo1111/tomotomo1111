public class ClassB3{
	public static void main(String[] args) {
		ScoreData3 id1 = new ScoreData3(50,70);
		System.out.println("英語特論の点数は"+id1.ReturnEngScore());
		System.out.println("統計解析学の点数は"+id1.ReturnMathScore());
		id1.ChangeEngScore(80);
		System.out.println("英語の再試験を実施し,点数を変更しました.");
		System.out.println("英語特論の点数は"+id1.ReturnEngScore());
	}
}

class ScoreData3{
	private double Eng, Math;
	final double rate = 0.8;
	public ScoreData3(double score1, double score2) {
		Eng = score1;
		Math = score2;
	}
	public double ReturnEngScore(){
		return Eng*rate;
	}
	public double ReturnMathScore(){
		return Math*rate;
	}
	public void ChangeEngScore(int score){
		Eng = score;
	}
}
	