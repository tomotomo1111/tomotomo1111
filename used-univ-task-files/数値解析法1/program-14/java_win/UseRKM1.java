import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
//  コンパイル: javac UseRKM1.java
//  実行: java UseRKM1
//
// として下さい．
//
// [実行例]
// Abs. error
// h           Euler
// 1.0000e-01  2.095333741540451e-02
// 1.0000e-02  2.029755482791829e-03
// 1.0000e-03  2.023126000112452e-04
// 1.0000e-04  2.022462466300112e-05
//
class UseRKM1 {
	public static void main(String[] args) {
		int n;
		// 分割数, 最大分割数
		int N, NMAX = 5000;
		// 分割の幅，最終時刻，初期値，初期時間
		double h = 0.0D, tlast = 0.5D, u0 = 1.0D, t = 0.0D;
		// 誤差用変数
		double err_euler = 0.0D, abserr;
		// 近似解格納変数
		double[] U = new double[NMAX + 1];

		// ルンゲ・クッタ型解法オブジェクト生成
		RKM rk = new RKM();

		try(PrintWriter pw_error = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-rk1-err.dat"))));
			PrintWriter pw_sol = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-rk1-sol.dat"))))){
			System.out.println("Abs. error");
			System.out.printf("%-10s  %-21s\n","h","Euler");
			// 分割数を 10 倍ずつ増加させて計算
			for (N = 5; N <= NMAX; N *= 10){
				h = tlast / N;
				rk.euler(N,h, u0, U);
				t = 0.0D; err_euler = 0.0D;
				for (n = 0 ; n <= N ; n++){
					abserr = Math.abs(U[n] - exactu(t));
					if (err_euler < abserr) err_euler = abserr;
					t += h;
				}
				System.out.printf("%6.4e  %17.15e\n", h, err_euler);
				//pw_error.printf("%17.15e  %17.15e\n", Math.log10(1.0D/h), Math.log10(err_euler));
				pw_error.printf("%17.15e  %17.15e\n", 1.0D/h, err_euler);
			}
			pw_error.close();
			
			// 近似解の出力
			t = 0.0D;
			for (n = 0 ; n <= NMAX ; n++){
				pw_sol.printf("%17.15e  %17.15e\n", t, U[n]);
				t += h;
			}
			pw_sol.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}

	// 真の解
	static double exactu(double t)
	{
		return(Math.exp(-t*t/2));
	}
}
