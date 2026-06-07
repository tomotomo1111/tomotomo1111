//
// 桁落ちサンプルプログラム Sample_cancelsd.java
//   コンパイル: javac Sample_cancelsd.java
//   実行: java Sample_cancelsd
//
import java.util.Scanner;

public class Sample_cancelsd{

	// 計算機イプシロン
	static double eps = Math.ulp(1.0D);

	public static void main(String[] args) {
		int rval;
		double [] coef = new double[3];
		double [] sol = new double[2];

		// 係数の入力 (Scanner クラスを使用してキーボードより入力)
		Scanner keyBoardScanner = new Scanner(System.in);
		System.out.println("2次方程式の係数を入力して下さい:");
		System.out.print("a = ");
		coef[0] = keyBoardScanner.nextDouble();
		System.out.print("b = ");
		coef[1] = keyBoardScanner.nextDouble();
		System.out.print("c = ");
		coef[2] = keyBoardScanner.nextDouble();

		// 2次方程式求解
		rval = getsol(coef, sol);

		if (rval == 0)
		{
			System.out.printf("重解: 解 = %18.16e\n", sol[0]);
		}
		else if (rval == 1)
		{
			System.out.println("異なる2つの虚数解（計算しない）");
		}
		else
		{
			System.out.printf("異なる2つの実数解:\n解1 = %18.16e\n解2 = %18.16e\n", sol[0], sol[1]);
		}
	}

	//
	//  二次方程式の求解メソッド getsol
	//
	//  【入力】
	//    coef[] = 係数が格納された doule 型配列
	//
	//  【出力】
	//    sol[] = 近似解が格納された double 型配列
	//            注) 重解を持つ場合は同じ値が格納される．
	//            注) 虚数解の場合は，0 が格納される．
	//
	//  【返却値】
	//    解の判別結果：0 -> 重解, 1 -> 異なる2つの虚数解,  2 -> 異なる2つの実数解
	//
	public static int getsol(double [] coef, double [] sol)
	{
		int hantei = 0;

		double a, b, c, d;

		a = coef[0]; b = coef[1]; c = coef[2];

		// 判別式の計算
		d = b * b - 4.0D * a * c;

		if (Math.abs(d) <= eps)
		{
		    // 重解を持つ場合
		    sol[0] = - b / (2.0D * a);
		    sol[1] = sol[0];
		}
		else if (d < -eps)
		{
		    // 共役複素数解解を持つ場合：解は計算しない
		    hantei = 1;
		    sol[0] = 0.0D;
		    sol[1] = 0.0D;
		}
		else
		{
		    // 2 つの異なる実数解をもつ場合
		    hantei = 2;
		    sol[0] = (-b + Math.sqrt(d)) / (2.0D * a);
		    sol[1] = (-b - Math.sqrt(d)) / (2.0D * a);
		}

		return(hantei);
	}
}
