import java.util.Scanner;

class BodyData{
	protected String name;
	protected double height;
	protected double weight;
	protected String male;
	
	public BodyData(String name, double height, double weight, String male) {
		this.name = name;
		this.height = height;
		this.weight = weight;
		this.male = male;
	}
	
	public void DataDisplay() {
		System.out.println("あなたの身体データは下記になります．");
		System.out.println(name + "さんの身体データを開示します．");
		System.out.println("身長：" + height);
		System.out.println("体重：" + weight);
		System.out.println("性別：" + male);
	}
}

class BMI extends BodyData{
	private double age;
	
	public BMI(String name, double height, double weight, String male, double age) {
		super(name, height, weight, male);
		this.age = age;
	}
	
	public void DataDisplay2() {
		super.DataDisplay();
		System.out.println("年齢：" + age);
	}
	
	private double BMIcalculation() {
		double BMI = weight / (height/100 * height/100);
		return BMI;
	}
	
	private void AdultsBodyFatness() {
		double BMI = BMIcalculation();
		
		if(BMI < 18.5) System.out.println("低体重(痩せ型)");
		if(18.5 <= BMI && BMI < 25) System.out.println("普通体重");
		if(25 <= BMI && BMI < 30) System.out.println("肥満(1度)");
		if(30 <= BMI && BMI < 35) System.out.println("肥満(2度)");
		if(35 <= BMI && BMI < 40) System.out.println("肥満(3度)");
		if(40 <= BMI) System.out.println("肥満(4度)");
	}
	
	public void BodyFatness() {
		System.out.println("あなたの肥満度は下記になります．");
		if(age < 16) System.out.println("測定不能");
		if(age >= 16) AdultsBodyFatness();
	}
}

public class ImputBodyData{
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
		
		BMI bmi = new BMI(name, height, weight, male, age);
		bmi.DataDisplay2();
		bmi.BodyFatness();
		
		} catch(Exception e) {
			System.exit(-1);
		}
		
	}
}