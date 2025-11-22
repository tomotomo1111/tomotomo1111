//
// コンパイル: javac MMC.java
// 実行: java UseMMC
//
// として下さい．
//
// [実行例] ※まったく同じ結果にはならないはずです
// Absolute error (Monte Carlo method)
//         N  Approx. value  Abs error
//         2  4.2310849e-02  5.8614342e-02
//         4  1.0953478e-01  8.6095927e-03
//         8  1.1238620e-01  1.1461007e-02
//        16  8.5778317e-02  1.5146874e-02
//        32  1.1728641e-01  1.6361219e-02
//        64  8.4575963e-02  1.6349227e-02
//       128  1.0021006e-01  7.1513336e-04
//       256  1.0608707e-01  5.1618841e-03
//       512  9.9283393e-02  1.6417970e-03
//      1024  1.0186352e-01  9.3833029e-04
//      2048  9.9858386e-02  1.0668040e-03
//      4096  9.9834061e-02  1.0911288e-03
//      8192  1.0134670e-01  4.2151295e-04
//     16384  1.0145732e-01  5.3212845e-04
//     32768  1.0092167e-01  3.5169765e-06
//     65536  1.0080844e-01  1.1675379e-04
//    131072  1.0117401e-01  2.4882408e-04
//    262144  1.0061959e-01  3.0559854e-04
//    524288  1.0085189e-01  7.3304173e-05
//   1048576  1.0095777e-01  3.2582042e-05
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

class UseMMC {

	public static void main(String[] args) {
		int i, k, N = 1;
		double mcval, mcerr;

		// 例題の設定
		int ndim = 5;	// 空間次元
		double exactval = Math.pow(1.0D - Math.exp(-1.0D), ndim);  // 真の積分値

		// 数値実験のための最大分点指数
		int kmax = 20;

		try{
			// 領域の定義
			double [] a = new double[ndim];
			double [] b = new double[ndim];
			for (i = 0 ; i < ndim ; i++){
				a[i] = 0.0D; b[i] = 1.0D;
			}
			
			// 乱数ベクトル格納配列
			double [] rvec = new double[ndim];

			// 関数値格納配列
			double [] fval = new double[(int)(Math.pow(2,kmax))];

			// 出力ファイルストリームオープン
			File ofile = new File("exc-mci.dat");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile)));
			File ofilee = new File("exc-mci-err.dat");
			PrintWriter pwe = new PrintWriter(new BufferedWriter(new FileWriter(ofilee)));

			// モンテカルロ法オブジェクトの生成
			MMC mimc = new MMC(ndim, a, b);

			// 例題計算
			pw.printf("%15s  %-20s  %-20s\n","N","Approx. value","Abs error");
			System.out.println("Absolute error (Monte Carlo method)");
			System.out.printf("%9s  %-9s  %-9s\n","N","Approx. value","Abs error");
			
			for (k = 1 ; k <= kmax ; k++){
				// 積分点数の更新
				N *= 2;

				// 関数の値の配列への格納
				for (i = 0 ; i < N ; i++){
					// 積分点 (乱数ベクトル) 生成
					mimc.randomvec(rvec);

					// 積分点における関数値計算
					fval[i] = f(rvec);
				}

				// 積分値の計算, 誤差の計算, 出力
				mcval = mimc.calc(N, fval); mcerr = Math.abs(mcval - exactval);
				pw.printf("%15d  %17.15e  %17.15e\n", N, mcval, mcerr);
				pwe.printf("%d  %17.15e\n", N, mcerr);
				System.out.printf("%9d  %9.7e  %9.7e\n", N, mcval, mcerr);
			}

			pw.close();
			pwe.close();
		}
		catch(Exception e){
			System.err.println("例外発生");
			e.printStackTrace();
		}

	}

	// 例題関数
	static double f(double [] x){
		int n = x.length;
		double rval = 0.0D;

		for (int i = 0 ; i < n ; i++) rval -= x[i];

		return(Math.exp(rval));
	}
}
