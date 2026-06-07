import java.util.Random;
import PackageC.Package6;

public class UsedPackageC {
	public static void main(String[] args) {
		Package6[] studentData = new Package6[5];
		String[] studentName = {"A","B","C","D","E"};
		for(int i=0; i<studentData.length; i++) {
			int[] score = {RanSc(),RanSc(),RanSc(),RanSc(),RanSc()};
			studentData[i] = new Package6(studentName[i], score);
		}
		AveDataDisplay(studentData);
	}
	
	public static int RanSc() {
		Random rand = new Random();
		return 60+rand.nextInt(40);
	}
	
	public static void AveDataDisplay(Package6[] array) {
		for(int i=0; i<array.length; i++) {
			System.out.println(array[i].student + "‚Ì•½‹Ï“_”‚Í" + array[i].ScoreAve() + "‚Å‚·D");
		}
	}
}