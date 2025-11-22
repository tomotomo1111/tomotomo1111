//
//  コンパイル: javac UseGL.java
//  実行: java UseGL
//
// として下さい．
//
// [実行結果]
// Approximate value = 4.999999999929175e-01
// Absolute error    = 7.082501252142492e-12
// Relative error    = 1.416500250428499e-11
//
class UseGL{

	public static void main(String[] args){
		int i, n = 8;
		double a = 1.0D, b = 2.0D;
		double exactval = 0.5D, glval;
		
		try{
			double [] x = new double[n];
			double [] fval = new double[n];

			// ガウス・ルジャンドル型積分公式オブジェクトの生成
			GL glf = new GL();

			// 積分点の取得
			glf.show_points(x);

			// 積分点における被積分関数値の格納
			for (i = 0 ; i < n ; i++) fval[i] = f(((b-a) * x[i] + a + b)/2);

			// ガウス・ルジャンドル型積分公式による積分値計算
			glval = glf.calc(a, b, fval);

			System.out.printf("Approximate value = %17.15e\n",glval);
			System.out.printf("Absolute error    = %17.15e\n",Math.abs(glval - exactval));
			System.out.printf("Relative error    = %17.15e\n",Math.abs(glval - exactval)/Math.abs(exactval));
		}
		catch(Exception e){
			System.err.println("例外発生");
			e.printStackTrace();
		}
	}

	// 例題関数
	static double f(double x)
	{
		return(1.0D/(x * x));
	}
}
