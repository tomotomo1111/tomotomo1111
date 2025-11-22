//
// 【コレスキー法クラス  Cholesky】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class Cholesky {
    //
    // コレスキー分解メソッド
    //  【入力】
    //   a = 係数行列 (正定値, 2次元配列，下三角部分のみ使用)
    //
    //  【出力】
    //   s = コレスキー分解行列（下三角行列）
    //
    //  【返却値】
    //   終了状態 (1 -> OK, 0 -> 異常終了)
    //
    int doDecomp(double[][] a, double[][] s){
	int rval = 1;
	int i, j, k;

	try{
	    // 行列の行次元数の取得
	    int n = a.length;

	    if (a[n-1].length != n){
		throw new IllegalArgumentException("正方行列ではありません．");
	    }
	    else{
		// [演習]コレスキー分解のプログラムを完成させてください
	    }
	}
	catch(Exception e){
	    System.out.println(e);
	    rval = 0;
	}

	return(rval);
    }

    //
    // コレスキー分解行列を用いた前進・後退代入メソッド
    //  【入力】
    //   s = コレスキー分解行列（下三角行列）
    //   b = 右辺ベクトル
    //
    //  【出力】 (実行後)
    //   b = 解ベクトル
    //
    //  【返却値】
    //   終了状態 (1 -> OK, 0 -> 異常終了)
    //
    int doGetSolution(double[][] s, double[] b){
	int rval = 1;
	int i, j, k;

	try{
	    // 行列次元の取得
	    int n = s.length;

	    if (b.length != n){
		throw new IllegalArgumentException("行列とベクトルの次元が合っていません．");
	    }
	    else{
		b[0] /= s[0][0];
		for(i = 1 ; i < n ; i++){
		    for(k = 0 ; k <= i - 1 ; k++){
			b[i] -= s[i][k] * b[k];
		    }
		    b[i] /= s[i][i];
		}
		b[n-1] /= s[n-1][n-1];

		for (i = n - 2 ; i >= 0 ; i--){
		    for (k = i + 1 ; k < n ; k++){
			b[i] -= s[k][i] * b[k];
		    }
		    b[i] /= s[i][i];
		}
	    }
	}
	catch(Exception e){
	    System.out.println(e);
	    rval = 0;
	}

	return(rval);
    }
    
    //
    // コレスキー法による求解メソッド
    //  【入力】
    //   a = 係数行列 (正定値, 2次元配列，下三角部分のみ)
    //   b = 右辺ベクトル (1次元配列)
    //
    //  【出力】
    //   b = 解ベクトル
    //
    //  【返却値】
    //   終了状態 (1 -> OK, 0 -> 異常終了)
    //
    int solve(double[][] a, double[] b)
    {
	int rval = 1;
	int n = a.length;
	int i, j, k;

	// コレスキー分解格納配列
	double [][] s = new double[n][];
	for (i = 0 ; i < n ; i++){
	    s[i] = new double[i+1];
	    for (j = 0 ; j <= i ; j++){
		s[i][j] = 0.0D;
	    }
	}

	// コレスキー分解
	rval = this.doDecomp(a, s);

	// 前進・後退消去による求解
	if(rval == 1) rval = this.doGetSolution(s, b);

	return(rval);
    }
}
