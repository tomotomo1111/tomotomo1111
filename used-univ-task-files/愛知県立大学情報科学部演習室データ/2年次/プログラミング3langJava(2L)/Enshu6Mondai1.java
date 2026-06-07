public class Enshu6Mondai1{
	public static void main(String[] args) {
		SeisekiData[] studentArray = new SeisekiData[3];
		int[] scoreData = {40,50,60};
		for(int i=0; i<studentArray.length; i++) {
			studentArray[i] = new SeisekiData(scoreData[i]);
		}
		System.out.println("‡Œv“_”‚Í" + SeisekiCalculate.ClassTotal(studentArray) + "“_‚Å‚·.");
	}
}

class SeisekiData{
	int Eng;
	public SeisekiData(int score1) {
		Eng = score1;
	}
}

class SeisekiCalculate{
	public static int ClassTotal(SeisekiData[] array) {
		int sum = 0;
		for(int i=0; i<array.length; i++) {
			sum += array[i].Eng;
		}
		return sum;
	}
}