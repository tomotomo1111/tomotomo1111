//
// 【多重積分用モンテカルロ法クラス  MMC】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
// 注) 直方体D=[a_0,b_0]x[a_1,b_2]x...x[a_{n-1},b_{n-1}]用のプログラムです．
//     
//
import java.util.Arrays;
public class MMC{

	// 空間次元
	int n;
	// 直方体各小区間左端点
	double [] a;
	// 直方体各小区間右端点
	double [] b;
	// 直方体面積|D|
	double areaD;

	//
	// コンストラクタ
	//  【入力】
	//    n = 空間次元
	//
	MMC(int ndim, double[] ai, double[] bi){
		n = ndim;
		a = new double[n];
		b = new double[n];

		// 配列のディープコピー
		a = Arrays.copyOf(ai, n);
		b = Arrays.copyOf(bi, n);
		
		// 面積|D|計算
		areaD = 1.0D;
		for(int i = 0; i < n; i++) areaD *= b[i]-a[i];
	}

	//
	// 積分計算メソッド
	//  【入力】
    //     M = 積分点数
	//     f = 関数値配列
	//
	//  【返却値】
	//     数値積分値
	//
	double calc(int M, double [] f)
	{
		// [演習] モンテカルロ法による数値積分計算メソッドを完成させてください．
		double rval;
		double temp = 0;
		for (int i = 0; i < M; i++) temp += f[i];
		rval = areaD * temp / M;

		return rval;
	}

	//
	// 積分領域内一様乱数ベクトル生成メソッド
	//  【入力】
	//     なし
	//
	//  【出力】
	//     x = n 次元一様乱数ベクトル
	//
	// * [a[i],b[i]] (i = 0, 1, ..., n -1) による直積集合の領域に対して発生させます．
	//
	void randomvec(double [] x)
	{
		int i;

		if (x.length != n)
		{
			throw new IllegalArgumentException("乱数ベクトル配列のサイズに問題があります．");
		}
		else
		{
			try{
				for (i = 0 ; i < n ; i++)
				{
					x[i] = (b[i] - a[i]) * Math.random() + a[i];
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				throw e;
			}
		}
	}
}
