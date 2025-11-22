import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
//  コンパイル: javac UseRKM24.java
//  実行: java UseRKM24
//
// として下さい．
//
// [実行例]
// Abs. error
// h           Heun                   Classical RK
// 1.0000e-01  3.732466747563557e-05  2.049930669656419e-09
// 1.0000e-02  1.751671488969819e-07  1.539879335155092e-13
// 1.0000e-03  2.243685681690977e-09  8.881784197001252e-16
// 1.0000e-04  2.291267175991152e-11  9.992007221626409e-15
//
class UseRKM24 {
	public static void main(String[] args) {
		int n;
		// 分割数, 最大分割数
		int N, NMAX = 5000;
		// 分割の幅，最終時刻，初期値，初期時間
		double h = 0.0D, tlast = 0.5D, u0 = 1.0D, t = 0.0D;
		// 誤差用変数
		double err_rk2 = 0.0D, err_rk4 = 0.0D;
		// ブッチャー配列（ホイン法）
		double[][] a2 = {{0.0D,0.0D},{1.0D,0.0D}};
		double[] b2 = {0.5D,0.5D};
		double[] c2 = {0.0D,1.0D};
		// 近似解格納変数 (ホイン法)
		double[] Urk2 = new double[NMAX + 1];
		// ブッチャー配列（古典的ルンゲ・クッタ法）
		double[][] a4 = {{0.0D,0.0D,0.0D,0.0D},{0.5D,0.0D,0.0D,0.0D},{0.0D,0.5D,0.0D,0.0D},{0.0D,0.0D,1.0D,0.0D}};
		double[] b4 = {1.0D/6, 1.0D/3, 1.0D/3, 1.0D/6};
		double[] c4 = {0.0D,0.5D,0.5D,1.0D};
		// 近似解格納変数 (1/6公式)
		double[] Urk4 = new double[NMAX + 1];

		try(PrintWriter pw_error = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-rk24-err.dat"))));
			PrintWriter pw_sol = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-rk24-sol.dat"))))){
			// ルンゲ・クッタ型解法オブジェクト生成
			RKM rk = new RKM();
			
			// 分割数を 10 倍ずつ増加させて計算
			System.out.println("Abs. error");
			System.out.printf("%-10s  %-21s  %-21s\n","h","Heun","Classical RK"); 
			for (N = 5; N <= NMAX; N *= 10){
				h = tlast / N;
				rk.explicit_rk(N, h, u0, a2, b2, c2, Urk2);
				rk.explicit_rk(N, h, u0, a4, b4, c4, Urk4);
				t = 0.0D; err_rk2 = 0.0D; err_rk4 = 0.0D;
				for (n = 0 ; n <= N ; n++){
					if (err_rk2 < Math.abs(Urk2[n] - exactu(t))) err_rk2 = Math.abs(Urk2[n] - exactu(t));
					if (err_rk4 < Math.abs(Urk4[n] - exactu(t))) err_rk4 = Math.abs(Urk4[n] - exactu(t));
					t += h;
				}
				// 1/分割幅 ホイン法の絶対誤差 古典的ルンゲ・クッタ法の絶対誤差 の形式で出力
				//pw_error.printf("%17.15e  %17.15e  %17.15e%n", Math.log10(1.0D/h), Math.log10(err_rk2), Math.log10(err_rk4));
				pw_error.printf("%17.15e  %17.15e  %17.15e%n", 1.0D/h, err_rk2, err_rk4);
				// 誤差の出力
				System.out.printf("%6.4e  %17.15e  %17.15e\n", h, err_rk2, err_rk4);
			}

			// 出力ファイルストリームのクローズ (絶対誤差)
			pw_error.close();
			
			// 近似解の出力
			t = 0.0D;
			for (n = 0 ; n <= NMAX ; n++){
				// 時刻  近似値(ホイン法) 近似値(古典的ルンゲ・クッタ法) の形式で出力
				pw_sol.printf("%17.15e  %17.15e  %17.15e\n", t, Urk2[n], Urk4[n]);
				t += h;
			}
			// 出力ファイルストリームのクローズ (近似解)
			pw_sol.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
		catch(IllegalArgumentException e){
			System.out.println(e);
		}
	}

	// 真の解
	static double exactu(double t)
	{
		return(Math.exp(-t*t/2));
	}
}
