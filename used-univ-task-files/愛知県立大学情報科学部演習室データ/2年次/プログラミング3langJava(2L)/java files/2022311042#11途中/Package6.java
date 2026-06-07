package PackageC;
public class Package6{
	public String student;
	public int[] scoreData = new int[5];
	public Package6(String name, int[] array) {
		student = name;
		scoreData = array;
	}
	
	public double ScoreAve() {
		int sum = 0;
		for(int i=0; i<scoreData.length;i++) {
			sum += scoreData[i];
		}
		return (double) sum / scoreData.length;
	}
}