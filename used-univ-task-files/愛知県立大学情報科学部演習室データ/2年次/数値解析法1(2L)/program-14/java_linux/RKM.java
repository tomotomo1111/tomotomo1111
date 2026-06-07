//
// 【ルンゲ・クッタ型解法クラス  RKM】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class RKM {

	//
	// オイラー法メソッド
	//  【入力】
	//   N  = 分割数 (n = 0,1,2,...,N)
	//   h  = 分割幅
	//   u0 = 初期値
	//
	//  【出力】
	//   U = 近似解が格納された配列
	//
	// * 講義スライド7ページ掲載アルゴリズム．
	//
	void euler(int N, double h, double u0, double[] U)
	{
		int n;
		double t = 0.0D;
		
		// [演習] オイラー法メソッドを完成させてください
		U[0] = u0;
		for (n = 0; n < U.length - 1; n++) {
			U[n + 1] = U[n] + h * F(n * h, U[n]);
		}
	}

	//
	// s段数陽的ルンゲ・クッタ法メソッド
	//  【入力】
	//   N  = 分割数 (n = 0,1,2,...,N)
	//   h  = 分割幅
	//   u0 = 初期値
	//   a  = ブッチャー配列a_{ij}
	//   b  = ブッチャー配列b_i
	//   c  = ブッチャー配列c_i
	//
	//  【出力】
	//   U = 近似解が格納された配列
	//
	// * 講義スライド27ページ参照
	//
	void explicit_rk(int N, double h, double u0, double[][] a, double[] b, double[] c, double[] U) throws IllegalArgumentException {
		int i,j,n;
		double t = 0.0D;
		int s = a[0].length;

		if (a[0][0] != 0.0D){
			throw new IllegalArgumentException("陽的ルンゲ・クッタ法メソッドです．陰的・半陰的は計算できません．");
		}
		else
		{
			double ktmp = 0.0D;
			double[] k = new double[s];
			

			// [演習] s段数陽的ルンゲ・クッタ法メソッドを完成させてください
			for (i = 0; i < k.length; i++) k[i] = 0.0D;
			for (n = 0; n < U.length - 1; n++) {
				U[0] = u0;
				k[0] = F(n * h, U[n]);
				for (i = 1; i < s; i++) {
					for (j = 0; j < i; j++) ktmp += a[i][j] * k[j] * h;
					k[i] = F(n * h + c[i] * h, U[n] + ktmp);
					ktmp = 0.0D;
				}
				for (i = 0; i < s; i++) ktmp += b[i] * k[i] * h;
				U[n + 1] = U[n] + ktmp;
				ktmp = 0.0D;
			}
		}
	}
	
	// 常微分方程式の右辺関数
	double F(double t, double u)
	{
		return(-t * u);
	}
}
