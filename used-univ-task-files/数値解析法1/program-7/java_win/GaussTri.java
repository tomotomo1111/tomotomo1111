//
//  コンパイル: javac GaussTri.java
//  実行: java UseGT
//
// として下さい． なおノルムの計算は，UtilityLA クラスに定義されている
// メソッド (static メソッド) を使用しています．UtilityLA.java を
// 同一ディレクトリ内に置いておいて下さい．
//
class UseGT {
	public static void main(String[] args) {
		// 行列の次元
		int n = 15;

		// 係数行列成分を格納する1次元配列
		double [] alpha = new double[n - 1];
		double [] beta = new double[n];
		double [] gamma = new double[n - 1];
		// 右辺ベクトル
		double [] b = new double[n];
		// 真の解ベクトル
		double [] exactx = new double[n];

		// ローカル変数
		int i;
		double exactnorm, errnorm;

		// 係数行列
		for (i = 0 ; i < n - 1 ; i++)
		{
			exactx[i] = 1.0D * i;
			alpha[i] = 1.0D;
			beta[i] = 5.0D;
			gamma[i] = 1.0D;
		}
		beta[n - 1] = 5.0D;
		exactx[n - 1] = 1.0D * (n - 1);

		// 右辺ベクトルの計算
		for (i = 0 ; i < n ; i++)
		{
			if (i == 0)
			{
				b[0] = beta[0] * exactx[0] + gamma[0] * exactx[1];
			}
			else if (i == n - 1)
			{
				b[n - 1] = alpha[n - 2] * exactx[n - 2] + beta[n - 1] * exactx[n - 1];
			}
			else
			{
				b[i] = alpha[i - 1] * exactx[i - 1] + beta[i] * exactx[i] + gamma[i] * exactx[i + 1];
			}
		}

		// 誤差ベクトル
		double [] errvec = new double[n];

		// ガウスの消去法オブジェクトの生成
		GaussTri gt = new GaussTri();

		// 真の解ベクトルのノルム計算
		exactnorm = UtilityLA.norm(exactx, 2);

		// ガウスの消去法による近似解ベクトル計算
		gt.solve(alpha, beta, gamma, b);

		// 相対誤差計算
		errvec = UtilityLA.vecpm(b, exactx, 'm');
		errnorm = UtilityLA.norm(errvec, 2);
		System.out.printf("相対誤差 = %17.15e\n",errnorm/exactnorm);
	}
}

//
// 【狭義優対角三重対角行列用ガウスの消去法クラス  GaussTridiagonal】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class GaussTri {
	//
	// 求解メソッド
	//  【入力】
	//   alpha = 下三角部分成分
	//   beta  = 対角成分
	//   gamma = 上三角部分成分
	//   b = 右辺ベクトル
	//
	//  【出力】 (実行後)
	//   b = 解ベクトル
	//
	void solve(double[] alpha, double[] beta, double[] gamma, double[] b)
	{
		// 行列次元の取得
		int n = beta.length;

		// ローカル変数
		int k;

		try
		{
			// 前進消去過程
			for (k = 0 ; k < n - 1 ; k++)
			{
				alpha[k] /= beta[k];
				beta[k + 1] -= alpha[k] * gamma[k];
				b[k + 1] -= alpha[k] * b[k];
			}

			// 後退代入
			b[n - 1] /= beta[n - 1];
			for (k = n - 2 ; k >= 0 ; k--)
			{
				b[k] -= gamma[k] * b[k + 1];
				b[k] /= beta[k];
			}
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
}
