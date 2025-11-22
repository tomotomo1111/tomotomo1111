//
// 情報落ちのサンプル Sample_losstg.java
//   コンパイル: javac Sample_losstg.java
//   実行: java Sample_losstg
//
public class Sample_losstg {

	public static void main(String[] args) {
		long n = 1L;
		int N = 16;
		float s, sn, err;

		// 真の値
		s = (float)(Math.PI * Math.PI / 6);

		// 部分和(n = 2, 2^2, ... , 2^10) による近似値の計算
		for (int i = 1 ; i <= N ; i++) {
			n *= 2L;
			sn = calcsum(n); // n = 2^i に対する部分和の近似値
			err = Math.abs(sn - s);	// 絶対誤差
			System.out.printf("項数 %5d: 近似値 = %9.7e (絶対誤差 = %9.7e)\n", n, sn, err);
		}
	}

	//
	//  無限級数近似計算メソッド calcsum
	//
	//  【入力】
	//    n = 項数 (long 型)
	//
	//  【出力】
	//    なし
	//
	//  【返却値】
	//    無限級数の近似値 (float 型)
	//
	public static float calcsum(long n)
	{
		float sn = 0.0F;

		// 無限級数の部分和の近似計算
		for (long k = 1 ; k <= n ; k++) {
			sn += 1.0F / (k * k);
		}

		return(sn);
	}
}
