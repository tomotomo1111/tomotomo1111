//
// コンパイル: javac UseSI.java
// 実行: java UseSI
//
// として下さい．なお実行には UtilityLA.java (完成品) が必要です．
//
// [実行例]
// Converged：num. of iterations = 80
// Approximate solution = (  4.999999999999993e-01  2.499999999999996e-01  )
//
class UseSI {
	public static void main(String[] args) {
		// 初期ベクトル
		double[] x0vec = {0.0D, 0.0D};
		// 解ベクトル
		double[] solvec = new double[x0vec.length];
				
		// 逐次反復法オブジェクトの生成
		SI si = new SI();

		// 逐次反復法による求解 (各ステップの値：ファイルへ出力される)
		solvec = si.solve(x0vec);

		// 解の出力
		System.out.printf("Approximate solution = (");
		for (int i = 0; i < solvec.length ; i++){
			System.out.printf("  %17.15e", solvec[i]);
		}
		System.out.printf("  )\n");
	}
}
