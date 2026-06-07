//
//  コンパイル: javac UseRBM.java
//  実行: java UseRBM
//
// として下さい．
//
// [実行結果]
// ロンバーグ積分法:近似値 = 5.000000183315112e-01 (相対誤差 = 3.666302239579977e-08)
//
class UseRBM{
	public static void main(String[] args) {
		int i, j, k;
		double a = 1.0D, b = 2.0D, h;
		double romk;

		// 真の解
		double exactval = 0.5D;

		// 分割にかかわる設定
		k = 3;

		// ロンバーグ積分法オブジェクトの生成
		RBM rbmobj = new RBM();

		// ロンバーグ積分法により近似値
		romk = rbmobj.calc(k, a, b);

		System.out.printf("ロンバーグ積分法:近似値 = %17.15e (相対誤差 = %17.15e)\n", romk, (Math.abs(exactval - romk)/Math.abs(exactval)));
	}
}
