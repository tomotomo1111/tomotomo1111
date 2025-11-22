//
// 【3次スプライン補間クラス CubicSpline】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
// また本クラスでは，連立一次方程式を解くのにGaussTriクラスを使用しています．
// GaussTri.java を，同じディレクトリ内に保存しておいてください
//
import java.util.Arrays;
public class CSpline {
	int n;			// 補間点数 - 1 (スライド n です)
	double [] x;	// 補間点 (x[i] = x_i)
	double [] c;	// 補間係数（c[i] = c_i = f'(x_i)） <- 11/14 修正
	double [] f;	// 補間データ (f[i] = f(x_i))
	double [] df = new double[2];	// 端点の導関数値
	boolean coef_flag;	// 係数フラグ (true -> 計算済, false -> 未計算)

	// コンストラクタ (フラグ初期化)
	CSpline()
	{
		coef_flag = false;
	}

	//
	// 補間値計算メソッド
	//  【入力】
	//   xcalc = 補間値を求めたい点
	//
	//  【返却値】
	//   補間値
	//
    	//   小区間の探索は，もっと良いアルゴリズムがありますよね．
	//   意欲のある人は，高速化を検討してください．
	//   なお，このメソッドを使用する前に，
	//   SplineCoef メソッドにより係数を求めておく必要があります．
	//
	double Interpolation(double xcalc)
	{
		int i = 1, j;
		double rval;

		if (xcalc < x[0] || xcalc > x[n])
		{
			throw new IllegalArgumentException("区間内の値を指定して下さい．");
		}
		else if (coef_flag == false)
		{
			throw new IllegalArgumentException("まず補間係数を計算しましょう．");
		}
		else
		{
			// 小区間の探索 (線形探索：もっと高速なアルゴリズムを使ったほうが...)
			for (j = 1 ; j < n ; j++)
			{
				if (xcalc >= x[j - 1] && xcalc <= x[j])
				{
					i = j;
					break;
				}
				else if (j == n-1)
				{
					i = j;
				}
			}

			// [演習] 14ページを参考に，補間値を計算するメソッドを完成させてください．なお，補間値は rval へ格納することを想定しています．
		}
	}

	//
	// 補間係数計算メソッド
	//  【入力】
	//   xp = 補間点 (1次元配列)
	//   fp = 補間データ
	//   dfp = 端点の導関数値
	//
	//  * 第6回講義使用スライド16ページにある連立一次方程式を解き，
	//    3次スプライン補間係数を求めています．
	//    連立一次方程式は，20ページに掲載されているアルゴリズムを実装した
	//    GaussTriクラスを用いて計算しています．
    	//    実行する前に，同じディレクトリにGaussTri.javaを保存しておいてください．
	//
	void SplineCoef(double[] xp, double[] fp, double[] dfp)
	{
		// 補間データ数の取得
		n = xp.length - 1;

		if (fp.length != n + 1)
		{
			throw new IllegalArgumentException("分点数とデータ数が合っていません．");
		}
		else if (dfp.length != 2)
		{
			throw new IllegalArgumentException("端点の導関数値配列のサイズが不正です．");
		}
		else
		{
			int i;
			x = new double[n + 1];
			c = new double[n + 1];
			f = new double[n + 1];

			// 補間点，データの格納 (Arrays.copyOf メソッドによるディープコピー)
			x = Arrays.copyOf(xp, n + 1);
			f = Arrays.copyOf(fp, n + 1);
			df = Arrays.copyOf(dfp, 2);

			// 行列，右辺ベクトルの生成
			double h1, h2; // h1=x_i-x_{i-1}, h2=x_{i+1}-x_i
			double[] alpha = new double[n];
			double[] beta = new double[n + 1];
			double[] gamma = new double[n];

			// [演習] 16ページを参考に，係数c_iを計算するメソッドを完成させてください．

			// 狭義優対角三重対角用 Gauss の消去法を用いた補間係数の計算
			GaussTri gt = new GaussTri();
			gt.solve(alpha, beta, gamma, c);

			coef_flag = true;
		}
	}
}
