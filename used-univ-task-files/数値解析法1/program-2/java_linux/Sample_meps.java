//
// マシンイプシロンサンプル Sample_meps.java
//
public class Sample_meps{
	public static void main(String[] args) {
		// マシンイプシロン
		double machine_epsilon = Math.ulp(1.0D);

		double x = 1.0D + machine_epsilon;
		double y = 1.0D + machine_epsilon / 2;
		System.out.printf("1 + e           = %18.16e\n", x);
		System.out.printf("1 + e / 2       = %18.16e\n", y);
	}
}
