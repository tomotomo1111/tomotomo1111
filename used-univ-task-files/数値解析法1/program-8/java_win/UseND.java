import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
// コンパイル: javac ND.java
// 実行: java UseND
//
// として下さい．
//
// [実行結果]
// Absolute error
// Div.           Forward        Backward       Central
// 1.0000000e+00  1.9524924e+00  1.0000000e+00  4.7624622e-01
// 5.0000000e-01  8.0853266e-01  5.7916071e-01  1.1468597e-01
// 2.5000000e-01  3.6996269e-01  3.1315458e-01  2.8404053e-02
// 1.2500000e-01  1.7719834e-01  1.6302955e-01  7.0843913e-03
// 6.2500000e-02  8.6744023e-02  8.3203902e-02  1.7700604e-03
// 3.1250000e-02  4.2919060e-02  4.2034160e-02  4.4245029e-04
// 1.5625000e-02  2.1347617e-02  2.1126400e-02  1.1060852e-04
// 7.8125000e-03  1.0645994e-02  1.0590691e-02  2.7651877e-05
// 3.9062500e-03  5.3160639e-03  5.3022380e-03  6.9129534e-06
// 1.9531250e-03  2.6563012e-03  2.6528447e-03  1.7282374e-06
// 9.7656250e-04  1.3277182e-03  1.3268541e-03  4.3205916e-07
// 4.8828125e-04  6.6375105e-04  6.6353502e-04  1.0801439e-07
// 2.4414063e-04  3.3184852e-04  3.3179451e-04  2.7003874e-08
// 1.2207031e-04  1.6591751e-04  1.6590401e-04  6.7512462e-09
// 6.1035156e-05  8.2957068e-05  8.2953690e-05  1.6889987e-09
//
class UseND
{
	public static void main(String[] args){
		// 刻み数の最大値 (hmax = 2^N)
		int N = 14;

		int k;
		double a = 1.0D, h = 1.0D;
		double exactval = f(a);
		double forval, backval, centval;
		double forerr, backerr, centerr;
		double [] fval = new double[2];


		// 差分オブジェクトの生成
		ND nd = new ND();

		try{
			// 出力ファイルストリームオープン
			File ofilef = new File("exc-fdf.dat");
			PrintWriter pwf = new PrintWriter(new BufferedWriter(new FileWriter(ofilef)));
			File ofileb = new File("exc-bdf.dat");
			PrintWriter pwb = new PrintWriter(new BufferedWriter(new FileWriter(ofileb)));
			File ofilec = new File("exc-cdf.dat");
			PrintWriter pwc = new PrintWriter(new BufferedWriter(new FileWriter(ofilec)));
			File ofilee = new File("exc-dif-err.dat");
			PrintWriter pwe = new PrintWriter(new BufferedWriter(new FileWriter(ofilee)));

			// 例題
			pwf.printf("%-21s  %-21s  %-21s\n","Div.","Forward diff.","Abs error");
			pwb.printf("%-21s  %-21s  %-21s\n","Div.","Backward diff.","Abs error");
			pwc.printf("%-21s  %-21s  %-21s\n","Div.","Central diff.","Abs error");
			System.out.println("Absolute error");
			System.out.printf("%-13s  %-13s  %-13s  %-13s\n","Div.","Forward","Backward","Central");
			for (k = 0 ; k <= N ; k++){
				// 前進差分
				fval[0] = f(a); fval[1] = f(a + h);
				forval = nd.ForwardDifference(h, fval); forerr = Math.abs(forval-exactval);
				pwf.printf("%17.15e  %17.15e  %17.15e\n", h, forval, forerr);

				// 後退差分
				fval[0] = f(a - h); fval[1] = f(a);
				backval = nd.BackwardDifference(h, fval); backerr = Math.abs(backval-exactval);
				pwb.printf("%17.15e  %17.15e  %17.15e\n", h, backval, backerr);

				// 中心差分
				fval[0] = f (a - h); fval[1] = f (a + h);
				centval = nd.CentralDifference(h, fval); centerr = Math.abs(centval - exactval);
				pwc.printf("%17.15e  %17.15e  %17.15e\n", h, centval, centerr);

				// 誤差の出力
				//pwe.printf("%17.15e  %17.15e  %17.15e  %17.15e\n", k * Math.log(2.0D), Math.log(forerr), Math.log(backerr), Math.log(centerr));
				pwe.printf("%17.15e  %17.15e  %17.15e  %17.15e\n", 1.0D/h, forerr, backerr, centerr);
				System.out.printf("%9.7e  %9.7e  %9.7e  %9.7e\n", h, forerr, backerr, centerr);

				// 刻み幅の更新
				h /= 2;
			}

			// 出力ファイルストリームのクローズ
			pwf.close();
			pwb.close();
			pwc.close();
			pwe.close();
		}
		catch (IOException e){
			System.out.println(e);
		}
	}

	// 例題関数
	static double f(double x){
		return(Math.exp(x));
	}
}
