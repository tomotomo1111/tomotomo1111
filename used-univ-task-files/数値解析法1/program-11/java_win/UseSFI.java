//
// コンパイル: javac UseSFI.java
// 実行: java UseSFI
//
// として下さい．
//
// [実行例]
// Converged (Steffensen): num. of iterations = 7
// Approximate solution = 5.000000000000019e-01
//
class UseSFI {
	public static void main(String[] args) {
		double x0 = 0.0D, sol;

		// ステファンセン反復オブジェクトの生成
		SFI sf = new SFI();

		// ステファンセン反復による求解 (各ステップの値：ファイルへ出力される)
		sol = sf.solve(x0);

		System.out.printf("Approximate solution = %17.15e\n", sol);
	}
}
