import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
//  コンパイル: javac NCF.java
//  実行: java UseNCF
//
// として下さい．
//
// [実行結果]
// Abusolute error
// Div.    trapezoid      Simpson        Midpoint
//      2  3.4722222e-02  4.6296296e-03  5.5555556e-02
//      4  8.9937642e-03  4.1761149e-04  1.6734694e-02
//      8  2.2708503e-03  2.9879044e-05  4.4520635e-03
//     16  5.6917013e-04  1.9433939e-06  1.1325101e-03
//     32  1.4238459e-04  1.2274543e-07  2.8440095e-04
//     64  3.5601917e-05  7.6920678e-09  7.1180757e-05
//
class UseNCF {

		public static void main(String[] args) {
			int i, k, kmax, N = 1;
			double exactval = 0.5D;
			double trapval, simpval, midval;
			double traperr, simperr, miderr;

			// ニュートン・コーツ公式オブジェクトの生成
			NCF ncm = new NCF();

			// 分割数の最大指数
			kmax = 6;

			// 関数値格納配列
			double[] fval = new double[(int)(Math.pow(2,kmax)) + 1];

			// 積分区間，分割幅初期値
			double a = 1.0D, b = 2.0D, h = b - a;

			try{
				// 出力ファイルストリームオープン
				File ofilet = new File("exc-trp.dat");
				PrintWriter pwt = new PrintWriter(new BufferedWriter(new FileWriter(ofilet)));
				File ofiles = new File("exc-sim.dat");
				PrintWriter pws = new PrintWriter(new BufferedWriter(new FileWriter(ofiles)));
				File ofilem = new File("exc-mid.dat");
				PrintWriter pwm = new PrintWriter(new BufferedWriter(new FileWriter(ofilem)));
				File ofilee = new File("exc-ncf-err.dat");
				PrintWriter pwe = new PrintWriter(new BufferedWriter(new FileWriter(ofilee)));

				// 例題計算
				pwt.printf("%-6s  %-21s  %-21s\n","Div","Approx.", "Abs error");
				pws.printf("%-6s  %-21s  %-21s\n","Div","Approx.", "Abs error");
				pwm.printf("%-6s  %-21s  %-21s\n","Div","Approx.", "Abs error");
				System.out.println("Absolute error");
				System.out.printf("%-6s  %-13s  %-13s  %-13s\n","Div.","trapezoid","Simpson","Midpoint");
				for (k = 1 ; k <= kmax ; k++){
					// 分割数・分割幅の更新
					N *= 2; h /= 2.0D;

					// 関数の値の配列への格納
					for (i = 0 ; i <= N ; i++){
						fval[i] = f(a + i * h);
					}

					// 積分値の計算, 誤差の計算, 出力
					trapval = ncm.TrapezoidalRule(N, h, fval); traperr = Math.abs(trapval - exactval);
					simpval = ncm.SimpsonRule(N, h, fval); simperr = Math.abs(simpval - exactval);
					midval = ncm.MidpointRule(N, h, fval); miderr = Math.abs(midval - exactval);
					pwt.printf("%6d  %17.15e  %17.15e\n", N, trapval, traperr);
					pws.printf("%6d  %17.15e  %17.15e\n", N, simpval, simperr);
					pwm.printf("%6d  %17.15e  %17.15e\n", N, midval, miderr);
					pwe.printf("%17.15e  %17.15e  %17.15e  %17.15e\n", 1.0D/h, traperr, simperr, miderr);
					System.out.printf("%6d  %9.7e  %9.7e  %9.7e\n", N, traperr, simperr, miderr);
				}

				pwt.close();
				pws.close();
				pwm.close();
				pwe.close();
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
		}

		// 例題の被積分関数
		static double f(double x)
		{
		        return(1.0D / (x * x));
		}
}
