//
// 【LU 分解クラス  LUdecomp】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class LUdecomp {

    double epsilon = Math.ulp(1.0D);

    //
    // LU分解メソッド
    //  【入力】
    //   a = 係数行列 (2次元配列)
    //   ipiv = 置換結果格納用配列
    //
    //  【出力】 (実行後)
    //   a = LU 分解済係数行列
    //   ipiv = 置換結果格納済配列
    //
    //  【返却値】
    //   終了状態 (1 -> OK, 0 -> 異常終了, -1 -> 係数行列が正則でない)
    //
    int doDecomp(double[][] a, int[] ipiv)
    {
	int rval = 1;
	int i, j, k, ip, ipivtemp;
	double amax, atemp;

	try {
	    // 行列の次元
	    int n = a[0].length;

	    // 置換結果ベクトルの初期化
	    for (k = 0 ; k < n ; k++) ipiv[k] = k;

	    // [演習] LU分解プログラムを完成させてください
	}
	catch (Exception e){
	    System.err.println(e);
	    rval = 0;
	}
	
	return(rval);
    }

    //
    // LU分解行列を用いた前進・後退代入メソッド
    //  【入力】
    //   a = LU 分解済係数行列
    //   ipiv = 置換結果格納配列
    //   b = 右辺ベクトル
    //
    //  【出力】 (実行後)
    //   b = 解ベクトル
    //
    //  【返却値】
    //   終了状態 (1 -> OK, 0 -> 異常終了)
    //
    int doGetSolution(double[][] a, int[] ipiv, double[] b)
    {
	int rval = 1;
	int j, k;
	
	try{
	    // 行列の次元を取得
	    int n = a[0].length;
	    double [] y = new double[n];

	    // 前進代入 (L y = P b を解く)
	    y[0] = b[ipiv[0]];
	    for (k = 1 ; k < n ; k++){
		y[k] = b[ipiv[k]];
		for (j = 0 ; j < k ; j++){
		    y[k] -= a[k][j] * y[j];
		}
	    }

	    // 後退代入 (U x = y を解く)
	    b[n - 1] = y[n - 1] / a[n-1][n-1];
	    for (k = n - 2 ; k >= 0 ; k--){
		b[k] = y[k];
		for (j = k + 1 ; j < n ; j++){
		    b[k] -= a[k][j] * b[j];
		}
		b[k] /= a[k][k];
	    }
	}
	catch(Exception e){
	    System.out.println(e);
	    rval = 0;
	}

	return(rval);
    }

    //
    // 求解メソッド
    //  【入力】
    //   a = 係数行列 (2次元配列)
    //   b = 右辺ベクトル
    //
    //  【出力】 (実行後)
    //   a = LU分解済係数行列
    //   b = 解ベクトル
    //
    //  【返却値】
    //   終了状態 (1 -> OK, 0 -> 異常終了, -1 -> 係数行列が特異)
    //
    int solve(double[][] a, double[] b){
	int rval;
	int n = a[0].length;
	int [] ipiv = new int[n];
	
	// LU 分解
	rval = this.doDecomp(a, ipiv);

	// 前進・後退消去による求解
	if (rval == 1) rval = this.doGetSolution(a, ipiv, b);
	
	return(rval);
    }
}
