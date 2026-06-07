//
//  コンパイル: javac UseChol.java
//  実行: java UseChol
//
// として下さい．ただし，Cholesky.javaを完成させておくこと．
// またノルムの計算は，UtilityLA クラスに定義されている
// メソッド (static メソッド) を使用しています．
// UtilityLA.java (完成品) を同一ディレクトリ内に置いておいて下さい．
//
// 【実行結果】
// 相対誤差 = 8.589293684407265E-12
//
import java.util.Arrays;

class UseChol{
    public static void main(String[] args){
	// 次元
	int n = 100;
	//int n = 5; // ヒルベルト行列の場合

	// 係数行列
	double [][] a = new double[n][n];
	// 右辺ベクトル
	double [] b = new double[n];
	// 真の解ベクトル
	double [] exactx = new double[n];

	// ローカル変数
	int i,j,rval;
	double exactnorm, errnorm;

	// 係数行列と真の解ベクトル
	for (i = 0 ; i < n ; i++){
	    for (j = 0 ; j < n ; j++){
		a[i][j] = Math.min(i + 1, j + 1);
		//a[i][j] = 1.0D / (i + j + 1);
	    }
	}
	Arrays.fill(exactx,1.1D);

	// 右辺ベクトル
	b = UtilityLA.mulMatVec(a,exactx);

	// コレスキー法オブジェクトの生成
	Cholesky cm = new Cholesky();

	// コレスキー法による求解
	rval = cm.solve(a, b);

	if (rval == 0){
	    System.out.println("異常終了しました．");
	}
	else{
	    // 誤差ベクトル
	    double [] errvec = new double[n];
	    
	    // 真の解ベクトルのノルム計算
	    exactnorm = UtilityLA.norm(exactx, 2);
	    
	    // 相対誤差計算
	    errvec = UtilityLA.vecpm(b, exactx, 'm');
	    errnorm = UtilityLA.norm(errvec, 2);
	    System.out.printf("相対誤差 = %17.15e\n",errnorm/exactnorm);
	}
    }
       
}
