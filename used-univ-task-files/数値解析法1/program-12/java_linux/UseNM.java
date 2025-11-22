//
// コンパイル: javac UseNM.java
// 実行: java UseNM
//
// として下さい．なお実行には，UtilityLA.java (完成品) が必要です．
//
// [実行例]
// Converged: num. of iterations = 8
// Approxmate soltion = 4.999999999999999e-01
//
// Converged: num. of iterations = 6
// Approximate solution = (5.000000000000000e-01, 2.500000000000000e-01)
//
class UseNM {
	public static void main(String[] args) {
	    double x0 = 0.0D;	// 初期値 (例題：6ページ)
		//double x0 = 9.9999999999999e-1; // 初期値 (例題：17ページ)
		double[] x0vec = {0.0D, 0.0D};	// 初期ベクトル (例題：13ページ)

		// Newton 法オブジェクトの生成
		NM nt = new NM();

		// 1次元ニュートン法による求解(各ステップの値：ファイルへ出力される)
		double sol1d = nt.solve(x0);
		System.out.printf("Approxmate soltion = %17.15e\n\n", sol1d);

		// 2次元ニュートン法による求解(各ステップの値：ファイルへ出力される)
		double[] sol2d = new double[2];
		sol2d = nt.solve(x0vec);
		System.out.printf("Approximate solution = (%17.15e, %17.15e)\n", sol2d[0], sol2d[1]);
	}
}
