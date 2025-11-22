//
//  コンパイル: javac UseDE.java
//  実行: java UseDE
//
// として下さい．
//
// [計算結果]
// Absolute error (DE formula, Integration on finite interval)
//    L  Approx. value  Abs error
//    4  1.1109878e+00  4.4432116e-01
//    8  6.8703258e-01  2.0365911e-02
//   16  6.6667475e-01  8.0795215e-06
//   32  6.6666667e-01  1.7230661e-13
//
// Absolute error (DE formula, Integration on infinite interval)
//    L  Approx. value  Abs error
//    4  3.3002445e+00  1.5865182e-01
//    8  3.1435080e+00  1.9153253e-03
//   16  3.1415927e+00  1.9715912e-08
//   32  3.1415927e+00  4.4408921e-16
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

class UseDE {
	
	public static void main(String[] args)
	{
		// 積分区間(有限区間例題)
		double a = 0.0D, b = 1.0D, h;

		// 積分区間(有限区間：両端に特異性ありの被積分関数)
		//double a = -1.0D, b = 1.0D, h;
		
		// 分割幅計算用変数
		double xsup = 4.0D; // 変換後関数が計算機イプシロン程度になる変数値
		//double xsup = 3.0D; // 両端に特異性が例の場合
		
		// 真の解
		double exactb = 2.0D/3;  // 有限区間積分
		//double exactb = Math.PI;  // 両端に特異性ありの場合
		double exacti = Math.PI; // 無限積分
		
		// 計算結果・絶対誤差格納変数
		double db, di, dberr, dierr;
		
		// 分割数関連変数
		int l, N = 1;
		
		// 誤差解析のための繰り返し用変数
		int k, kmax = 4;
		
		try{
			// 被積分関数格納用配列
			double [] fvalb = new double[(int)(Math.pow(2,kmax+1)) + 1];
			double [] fvali = new double[(int)(Math.pow(2,kmax+1)) + 1];
			
			// 出力ファイルストリームオープン(有限区間積分結果出力用)
			File ofileb = new File("exc-de-bound.dat");
			PrintWriter pwb = new PrintWriter(new BufferedWriter(new FileWriter(ofileb)));
			
			// 出力ファイルストリームオープン(無限積分結果出力用)
			File ofilei = new File("exc-de-inf.dat");
			PrintWriter pwi = new PrintWriter(new BufferedWriter(new FileWriter(ofilei)));

			// DE 積分公式オブジェクトの生成
			DE de = new DE();

			// 例題計算(有限区間積分)
			pwb.printf("%4s  %-20s  %-20s\n","L","Approx. value","Abs error");
			System.out.println("Absolute error (DE formula, Integration on finite interval)");
			System.out.printf("%4s  %-9s  %-9s\n","L","Approx. value","Abs error");
			for (k = 1; k <= kmax; k++){
				N *= 2;
				h = xsup/N;

				// 被積分関数値の配列への格納
				for (l = -N ; l <= N ; l++){
					fvalb[l + N] = fb(de.TFBound(a, b, l * h));
				}
				
				// DE公式による計算
				db = de.DEBD(a, b, h, N, N, fvalb);
				dberr = Math.abs(db - exactb);
				pwb.printf("%4d  %17.15e  %17.15e\n", 2*N, db, dberr);
				System.out.printf("%4d  %9.7e  %9.7e\n", 2*N, db, dberr);
			}
			pwb.close();
			
			// 例題計算(無限積分)
			pwi.printf("%4s  %-20s  %-20s\n","L","Approx. value","Abs error");
			System.out.println("\nAbsolute error (DE formula, Integration on infinite interval)");
			System.out.printf("%4s  %-9s  %-9s\n","L","Approx. value","Abs error");
			N = 1;
			for (k = 1; k <= kmax; k++){
				N *= 2;
				h = xsup/N;
				
				// 被積分関数値の配列への格納
				for (l = -N ; l <= N ; l++){
					fvali[l + N] = fi(de.TFInf(l * h));
				}
				
				// DE公式による計算
				di = de.DEIF(h, N, N, fvali);
				dierr = Math.abs(di - exacti);
				pwi.printf("%4d  %17.15e  %17.15e\n", 2*N, di, dierr);
				System.out.printf("%4d  %9.7e  %9.7e\n", 2*N, di, dierr);
			}
			pwi.close();
		
		}
		catch(Exception e){
			System.err.println("例外発生");
			e.printStackTrace();
		}
	}
	
	// 有限区間関数
	static double fb(double x)
	{
		return(Math.sqrt(x));
		//return(1.0D / Math.sqrt(1.0D - x * x)); // x=-1,1に特異性あり
	}
	
	// 半無限区間関数
	static double fi(double x)
	{
		return(1.0D / (1.0D + x * x));
	}
}
