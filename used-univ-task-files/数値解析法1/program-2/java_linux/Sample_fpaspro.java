//
// 結合律確認サンプルプログラム Sample_fpaspro.java
//   コンパイル: javac Sample_fpaspro.java
//   実行: java Sample_fpaspro
//
public class Sample_fpaspro
{
	public static void main(String[] args) {
		double a = Math.pow(2,53);
		double b = 1.0D;
		double c = 1.0D;
		double d = a + b; d += c;
		double e = a; e += b + c;

		System.out.printf("(a + b) + c = %17.15e\n",d);
		System.out.printf("a + (b + c) = %17.15e\n",e);
	}
}
