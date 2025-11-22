//
// 【ガウスの消去法クラス  GaussEl】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class GaussEl {

        double epsilon = Math.ulp(1.0D);

	//
	// 求解メソッド
	//  【入力】
	//   a = 係数行列 (2次元配列)
	//   b = 右辺ベクトル
	//
	//  【出力】 (実行後)
	//   b = 解ベクトル
	//
	//  【返却値】
	//   終了状態 (1 -> OK, 0 -> 異常終了, -1 -> 係数行列が特異)
	//
	int getSol(double[][] a, double[] b)
	{
		// 返却値 (初期値は正常)
		int rval = 1;
		// 行列次元の取得
		int n = a[0].length;

		// ローカル変数
		int i, j, k, ip;
		double amax, atemp, btemp;

		// 注) 配列を使用しているため例外処理を付けましたが，
		//     Java 言語に詳しい人は，もう少しまともな例外処理に
		//     修正して下さい．
		try
		{
			// 前進消去過程
			for (k = 0 ; k < n - 1 ; k++)
			{
				// 部分ピボット探索
				amax = Math.abs(a[k][k]);
				ip = k;
				for (i = k + 1 ; i < n ; i++)
				{
					if (Math.abs(a[i][k]) > amax)
					{
						amax = Math.abs(a[i][k]); ip = i;
					}
				}

				if (amax <= epsilon)
				{
					// 係数行列は特異 (終了)
					rval = -1;
					return(rval);
				}
				else if (ip != k)
				{
					// 行の交換
					for (j = k ; j < n ; j++)
					{
						atemp = a[k][j];
						a[k][j] = a[ip][j];
						a[ip][j] = atemp;
					}
					btemp = b[k];
					b[k] = b[ip];
					b[ip] = btemp;
				}

				// [演習] 前進消去部分を記述してください
			}

			// 後退代入
			// [演習] 後退代入部分を記述してください
		}
		catch (Exception e)
		{
			System.err.println(e);
			rval = 0;
		}

		return(rval);
	}

}
