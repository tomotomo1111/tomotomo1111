public class ClassB4{
	public static void main(String[] args) {
		ScoreData4 id1 = new ScoreData4(50,70);
		System.out.println("英語特論の点数は"+id1.ReturnEngScore());
		System.out.println("統計解析学の点数は"+id1.ReturnMathScore());
		System.out.println("英語特論の成績点数は"+Calculate.ScoreCorrection(id1.ReturnEngScore()));
		System.out.println("統計解析学の成績点数は"+Calculate.ScoreCorrection(id1.ReturnMathScore()));
	}
}

class ScoreData4{
	private double Eng, Math;
	public ScoreData4(double score1, double score2){
		Eng = score1;
		Math = score2;
	}
	public double ReturnEngScore(){
		return Eng;
	}
	public double ReturnMathScore(){
		return Math;
	}
}

class Calculate{
	static final double rate = 0.8;
	public static double ScoreCorrection(double score) {
		return score*rate;
	}
}