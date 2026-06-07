import java.util.Scanner;
import PackageBmi.BmiCalculation;

public class CheckBodyHealth{
	public static void main(String[] args) {
		System.out.println("あなたの身体データを基に肥満度を計測します．");
		System.out.println("あなたの身体データを入力してください．");
		Scanner scan = new Scanner(System.in);
		System.out.println("名前は??");
		String name = scan.next();
		System.out.println("身長は何cmですか?");
		try {
		double height = Double.parseDouble(scan.next());
		System.out.println("体重は何kgですか?");
		double weight = Double.parseDouble(scan.next());
		System.out.println("性別は??");
		String male = scan.next();
		System.out.println("何歳ですか?");
		double age = Double.parseDouble(scan.next());
		
		BmiCalculation bmi = new BmiCalculation(name, height, weight, male, age);
		bmi.DataDisplay();
		System.out.println(bmi.BodyFatness());
		
		} catch(Exception e) {
			System.exit(-1);
		}
		int totalYes = 0;
		System.out.println("生活習慣に関してインタビューします．");
		System.out.println("はい/いいえ で答えてください．");
		System.out.println("1日の運動時間は1時間以下ですか?");
		if(scan.next().equals("はい")) totalYes++;
		System.out.println("1日にアルコール飲料を2L(リットル)以上飲みますか?");
		if(scan.next().equals("はい")) totalYes++;
		System.out.println("インスタント食品を週6回以上食べますか?");
		if(scan.next().equals("はい")) totalYes++;
		System.out.println("1日の睡眠時間は6時間以下ですか?");
		if(scan.next().equals("はい")) totalYes++;
		System.out.println("野菜を全く食べない日はありますか?");
		if(scan.next().equals("はい")) totalYes++;
		System.out.println("インタビューの結果から");
		if(totalYes >=4) {
			System.out.println("生活習慣を至急改善する必要があります．");
		} else {
			System.out.println("生活習慣を改善するように心掛けましょう");
		}
	}
}