//
//  コンパイル: javac UseGauss.java
//  実行: java UseGauss
//
// として下さい．ただし，GaussEl.javaを完成させておく必要があります．
// またノルムの計算は，UtilityLA クラスに定義されているメソッド (static メソッド) を
// 使用しています．UtilityLA.java（完成品）を同一ディレクトリ内に置いておいて下さい．
//
// 【実行結果】
// 相対誤差 = 8.5892936844072650e-12
//
import java.util.Arrays;
class UseGauss {
	public static void main(String[] args) {
		// 行列の次元
		int n = 100;

		// 係数行列
		double [][] a = new double[n][n];
		// 右辺ベクトル
		double [] b = new double[n];
		// 真の解ベクトル
		double [] exactx = new double[n];

		// ローカル変数
		int i, j, rval;
		double exactnorm, errnorm;

		// 係数行列と真の解ベクトル
		for (i = 0 ; i < n ; i++)
		{
			for (j = 0 ; j < n ; j++)
			{
				a[i][j]=Math.min(i+1, j+1);
			}
		}
		Arrays.fill(exactx,1.1D);


		// 右辺ベクトル
		b = UtilityLA.mulMatVec(a,exactx);

		// 誤差ベクトル
		double [] errvec = new double[n];

		// ガウスの消去法オブジェクトの生成
		GaussEl ge = new GaussEl();

		// 真の解ベクトルのノルム計算
		exactnorm = UtilityLA.norm(exactx, 2);

		// ガウスの消去法による近似解ベクトル計算．
		// 計算終了後，近似解ベクトルは配列 b に格納されています．
		rval = ge.getSol(a, b);

		if (rval == 0)
		{
			System.out.println("異常終了しました．");
		}
		else if (rval == -1)
		{
			System.out.println("係数行列：正則ではありません．");
		}
		else
		{
			// 相対誤差計算
			errvec = UtilityLA.vecpm(b, exactx, 'm');
			errnorm = UtilityLA.norm(errvec, 2);
			System.out.printf("相対誤差 = %18.16e\n",errnorm/exactnorm);
		}
	}
}
