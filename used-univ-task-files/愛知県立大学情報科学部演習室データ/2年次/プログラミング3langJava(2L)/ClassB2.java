public class ClassB2{
	public static void main(String[] args) {
		ScoreData2 id1 = new ScoreData2(50,70);
		System.out.println("英語特論の点数は"+id1.ReturnEngScore());
		System.out.println("統計解析学の点数は"+id1.ReturnMathScore());
		id1.ChangeEngScore(60);
		System.out.println("英語特論の点数は"+id1.ReturnEngScore());
		System.out.println("統計解析学の点数は"+id1.ReturnMathScore());
	}
}

class ScoreData2{
	private int Eng, Math;
	public ScoreData2(int score1, int score2){
		Eng = score1;
		Math = score2;
	}
	public int ReturnEngScore(){
		return Eng;
	}
	public int ReturnMathScore(){
		return Math;
	}
	
	public void ChangeEngScore(int score) {
		Eng = score;
	}
}