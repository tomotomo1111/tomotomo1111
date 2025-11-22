//
// コンパイル: javac UseAKI.java
// 実行: java UseAKI
//
// として下さい．
//
// [実行例]
// Converged（simple iteration): num. of iterations = 244
// Approximate solution = 4.999999999999882e-01
//
// Converged（simple + Aitken): num. of iterations = 117
// Approximate solution = 4.999999999999846e-01
//
class UseAKI{
	public static void main(String[] args) {
		// スライド例題 (22ページ)
		double x0 = 0.0D, sol;

		// 逐次反復法＋エイトケン加速オブジェクトの生成
		AKI ak = new AKI();

		// 逐次反復法による求解 (各ステップの値：ファイルへ出力される, 22ページ用)
		sol = ak.solve(x0,false);
		System.out.printf("Approximate solution = %17.15e\n\n", sol);

		// 逐次反復法＋エイトケン加速による求解 (各ステップの値：ファイルへ出力される, 22ページ用)
		sol = ak.solve(x0,true);
		System.out.printf("Approximate solution = %17.15e\n", sol);
	}
}
