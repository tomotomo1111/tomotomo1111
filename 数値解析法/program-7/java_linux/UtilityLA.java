//
// 【行列・ベクトル演算クラス UtilityLA】
//
// 同一ディレクトリ内の Java プログラムから利用できます．
// 全て static クラスであるため，オブジェクトを明示的に
// 作成する必要はありません．クラス内メソッドの仕様については，
// コメントを参照して下さい．
//
// 注) かっこ悪いプログラムですね．センスのある人は，
//     Java らしいプログラムへ書き直してください．
//
public class UtilityLA {

	//
	// 行列・行列積メソッド
	//  【入力】
	//   a = m x l 行列 (2次元配列)
	//   b = l x n 行列 (2次元配列)
	//
	//  【返却値】
	//   c = a * b (m x n 行列, 2次元配列)
	//
	static double[][] mulMatMat(double[][] a, double[][] b)
	{
		int i, j, k;

		// 行列の次元取得
		int m = a.length;
		int l = a[0].length;
		int n = b[0].length;

		// 次元のチェック
		if (m == 0 || l == 0 || n == 0)
		{
			throw new IllegalArgumentException("行列の次元は 0 より大にして下さい．");
		}
		else if (b.length != l)
		{
			throw new IllegalArgumentException("行列の次元数が合っていません．");
		}
		else
		{
			double[][] c = new double[m][n];

			// 積の計算
			for (i = 0 ; i < m ; i++)
			{
				for (j = 0 ; j < n ; j++)
				{
					c[i][j] = 0.0D;

					for (k = 0 ; k < l ; k++)
					{
						c[i][j] += a[i][k] * b[k][j];
					}
				}
			}
			return(c);
		}
	}

	//
	// 行列・ベクトル積メソッド
	//  【入力】
	//   a = m x n 行列 (2次元配列)
	//   x = n 次元ベクトル (1次元配列)
	//
	//  【返却値】
	//   y = a * x (m 次元ベクトル, 1次元配列)
	//
	static double[] mulMatVec(double[][] a, double[] x)
	{
		int i, j;

		// 行列・ベクトルの次元取得
		int m = a.length;
		int n = a[0].length;

		// 次元のチェック
		if (m == 0 || n == 0)
		{
			throw new IllegalArgumentException("行列の次元は 0 より大にして下さい．");
		}
		else if (x.length != n)
		{
			throw new IllegalArgumentException("ベクトルの次元数が行列と合っていません．");
		}
		else
		{
			double [] y = new double[m];

			for (i = 0 ; i < m ; i++)
			{
				y[i] = 0.0D;
				for (j = 0 ; j < n ; j++) y[i] += a[i][j] * x[j];
			}

			return(y);
		}
	}

	//
	// ベクトルの加減算計算メソッド
	//  【入力】
	//   x = n 次元ベクトル (1次元配列)
	//   y = n 次元ベクトル (1次元配列)
	//   cp = 加減算の種類 (char 型, cp = 'p' - > 加算, cp = 'm' -> 減算)
	//
	//  【返却値】
	//   x + y または x - y (1次元配列)
	//
	static double[] vecpm(double[] x, double[] y, char cp)
	{
		int i;

		// ベクトル次元の取得
		int n = x.length;

		if (n == 0)
		{
			throw new IllegalArgumentException("配列を使用して下さい．");
		}
		else if (y.length != n)
		{
			throw new IllegalArgumentException("配列サイズが一致していません．");
		}
		else if (cp != 'p' && cp != 'm')
		{
			throw new IllegalArgumentException("演算種類を表す記号は p または m です．");
		}
		else
		{
			double [] rvec = new double[n];

			if (cp == 'p')
			{
				for (i = 0 ; i < n ; i++)
				{
					rvec[i] = x[i] + y[i];
				}
			}
			else
			{
				for (i = 0 ; i < n ; i++)
				{
					rvec[i] = x[i] - y[i];
				}
			}

			return(rvec);
		}
	}

	//
	// ベクトルの1次結合メソッド (オーバーロード)
	//  【入力】
	//   x = n 次元ベクトル (1次元配列)
	//   alpha = x に対する係数
	//   y = n 次元ベクトル (1次元配列)
	//   beta = y に対する係数
	//
	//  【返却値】
	//   alpha * x + beta * y (1次元配列)
	//
	static double[] veclc(double[] x, double alpha, double[] y, double beta)
	{
		int i;

		// ベクトル次元の取得
		int n = x.length;

		if (n == 0)
		{
			throw new IllegalArgumentException("配列を使用して下さい．");
		}
		else if (y.length != n)
		{
			throw new IllegalArgumentException("配列サイズが一致していません．");
		}
		else
		{
			double [] rvec = new double[n];

			for (i = 0 ; i < n ; i++)
			{
				rvec[i] = alpha * x[i] + beta * y[i];
			}

			return(rvec);
		}
	}

	//
	// ベクトル内積計算メソッド
	//  【入力】
	//   x = n 次元ベクトル (1次元配列)
	//   y = n 次元ベクトル (1次元配列)
	//
	//  【返却値】
	//   内積値
	//
	static double dot(double[] x, double[] y)
	{
		int i;
		double rval = 0.0D;

		// ベクトル次元の取得
		int n = x.length;

		// 次元のチェック
		if (n == 0)
		{
			throw new IllegalArgumentException("ベクトル次元は 0 より大にして下さい．");
		}
		else if (y.length != n)
		{
			throw new IllegalArgumentException("ベクトルの次元が合っていません．");
		}
		else
		{
			for (i = 0 ; i < n ; i++) rval += x[i] * y[i];
		}

		return(rval);
	}

	//
	// ベクトルノルム計算メソッド
	//  【入力】
	//   x = n 次元ベクトル (1次元配列)
	//   p = ノルムの種類 (p > 0 -> p ノルム, p = 0 -> 最大値ノルム)
	//
	//  【返却値】
	//   ベクトルノルム値
	//
	static double norm(double[] x, int p)
	{
		int i;
		double rval = 0.0D;

		// ベクトル次元の取得
		int n = x.length;

		// 次元のチェック
		if (n == 0)
		{
			throw new IllegalArgumentException("ベクトル次元は 0 より大にして下さい．");
		} // ノルム種類のチェック
		else if (p < 0)
		{
			throw new IllegalArgumentException("ノルムの種類は 0 以上の値で指定して下さい．");
		} // 1 ノルム
		else if (p == 1)
		{
		    for (i = 0; i < n ; i++) rval += Math.abs(x[i]);
		} // 2 ノルム
		else if (p == 2)
		{
			rval = dot(x, x);
			rval = Math.sqrt(rval);
		} // 最大値ノルム
		else if (p == 0)
		{
		  	rval = Math.abs(x[0]);
			for (i = 1; i < n ; i++){
			    if (Math.abs(x[i]) > rval) rval = Math.abs(x[i]);
			}
		}
		else // p ノルム
		{
			for (i = 0 ; i < n ; i++) rval += Math.pow(Math.abs(x[i]), p);
			rval = Math.pow(rval, 1.0D / p);
		}

		return(rval);
	}

	//
	// 行列ノルム計算メソッド（オーバーロード）
	//  【入力】
	//   a = m 行 n 列行列 (2次元配列)
	//   p = ノルムの種類 (p = 1 -> 1ノルム，p = 0 -> 最大値ノルム，p = 3 -> フロベニウスノルム)
    	//   注) 2ノルムは，実装できません
	//
	//  【返却値】
	//   行列ノルム値
	//
	static double norm(double[][] a, int p)
	{
	    int i,j;
		double rval = 0.0D, rvaltmp = 0.0D;

		// 次元の取得
		int m = a.length;
		int n = a[0].length;

		// 次元のチェック
		if (m == 0 || n == 0)
		{
			throw new IllegalArgumentException("行列の次元は 0 より大にして下さい．");
		} // 1 ノルム
		else if (p == 1)
		{
		        for (j = 0; j < n ; j++){
			    	rvaltmp = 0.0D;
			    	for (i = 0; i < m ; i++) rvaltmp += Math.abs(a[i][j]);
			    	if (rvaltmp > rval) rval = rvaltmp;
		        }
		} // 2 ノルム
		else if (p == 2)
		{
			throw new IllegalArgumentException("実装していません．");
		} // 最大値ノルム
		else if (p == 0)
		{
		        for (i = 0; i < m ; i++){
			    	rvaltmp = 0.0D;
			    	for (j = 0; j < n ; j++) rvaltmp += Math.abs(a[i][j]);
			    	if (rvaltmp > rval) rval = rvaltmp;
		        }
		} // フロベニウスノルム
		else if (p == 3)
		{
		        for (i = 0; i < m ; i++){
			    	for (j = 0; j < n ; j++) rval += Math.pow(a[i][j], 2);
				}
				rval = Math.sqrt(rval);
		}
		else // その他
		{
			throw new IllegalArgumentException("2番目の引数には -1, 0, 1 いずれかを入力してください．");		    
		}

		return(rval);
	}
}
