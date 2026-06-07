//
// 【最小二乗近似クラス LSQ】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
// また，第2回講義演習で作成したGaussEl.java (完成品) を，
// 同じディレクトリにおいてください．
//
public class LSQ {
	int m;		// 最小二乗近似多項式の次数
	double[] c;	// 係数 c
	boolean coef_flag;	// 係数フラグ (計算済み -> true, 未計算 -> false)

	// コンストラクタ
	// 【入力値】
	//   mdim = 最小二乗近似多項式の次数
	//
	//  * 係数配列の生成，フラグの初期化を行う．
	LSQ(int mdim){
		m = mdim;
		c = new double[m + 1];
		coef_flag = false;
	}

	//
	// 係数計算メソッド
	//  【入力値】
	//    x = 分点
	//    f = 関数値データ
	//
	//  【返却値】
	//   終了状態 (1 -> OK, 0 -> 異常終了, -1 -> 正規方程式の解が一意でない)
	//
	int calcCoef(double[] x, double[] f) {
		// 返却値 (初期値は正常)
		int rval = 1;
		
		// データ数
		int n = x.length;

		if (coef_flag == false)
		{
			try
			{
				if (f.length != n)
				{
					throw new IllegalArgumentException("配列サイズが一致していません．");
				}
				else
				{
					int i, j, k, l;

					// 行列 A
					double[][] a = new double[n][m + 1];

					// 行列 A 各要素の格納
					for(i = 0 ; i < n ; i++) {
						for (j = 0 ; j <= m ; j++) {
							a[i][j] = Math.pow(x[i], j);
						}
					}
					
					// [演習] 27ページを参考に，最小二乗近似の係数 c_i を計算するメソッドを完成させてください．

					// 部分ピボット付きガウスの消去法クラスのインスタンス生成
					GaussEl ge = new GaussEl();

					// 部分ピボット付きガウスの消去法により正規方程式求解
					rval = ge.getSol(ata, c);

					// 解が求まった場合：フラグを true にする．
					if (rval == 1) coef_flag = true;
				}
			}
			catch (Exception e)
			{
				System.err.println(e);
				rval = 0;
			}
		}

		return(rval);
	}

	//
	// 係数出力メソッド
	//  【出力値】
	//    c = 係数を格納した配列
	//
	void showCoef(double[] coef) {
		if (coef_flag == false)
		{
			throw new IllegalArgumentException("まず係数を計算しましょう．");
		}
		else if (coef.length != m + 1)
		{
			throw new IllegalArgumentException("配列サイズが不正です．");
		}
		else
		{
			for (int i = 0 ; i <= m ; i++) {
				coef[i] = c[i];
			}
		}
	}

	//
	//  最小二乗近似多項式の近似値計算メソッド
	//  【入力値】
	//    x = 関数値を計算する点
	//
	//  【返却値】
	//    最小二乗近似多項式の値
	//
	double calcLSP(double x) {
		double p = 0.0D;

		if (coef_flag == false)
		{
			throw new IllegalArgumentException("まず係数を計算しましょう．");
		}
		else
		{
			// [演習] 近似値を計算するメソッドを完成させて下さい．
		}

		return(p);
	}

}
