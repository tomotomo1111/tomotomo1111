//
// 【差分クラス ND】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class ND {
	//
	// 前進差分値計算メソッド
	//  【入力】
	//   h = 差分幅
	//   f = 関数値格納配列 (f[0] = f(a), f[1] = f(a + h))
	//
	//  【返却値】
	//   a における前進差分値
	//
	// * 第7回講義スライド4ページに掲載されている前進差分による
	//   数値微分メソッドです．
	//
	double ForwardDifference(double h, double[] f)
	{
		if (h <= Double.MIN_VALUE){
			throw new IllegalArgumentException("分割幅が小さすぎます．");
		}
		else if (f.length != 2){
			throw new IllegalArgumentException("配列サイズは 2 にして下さい．");
		}
		else{
			return((f[1] - f[0])/h);
		}
	}

	//
	// 後退差分値計算メソッド
	//  【入力】
	//   h = 差分幅
	//   f = 関数値格納配列 (f[0] = f(a - h), f[1] = f(a))
	//
	//  【返却値】
	//   a における後退差分値
	//
	// * 第7回講義スライド4ページに掲載されている後退差分による
	//   数値微分メソッドです．
	//
	double BackwardDifference(double h, double[] f)
	{
		if (h <= Double.MIN_VALUE){
			throw new IllegalArgumentException("分割幅が小さすぎます．");
		}
		else if (f.length != 2){
			throw new IllegalArgumentException("配列サイズは 2 にして下さい．");
		}
		else{
			// [演習] 講義スライドp.3を参考にして後退差分近似のプログラムを作成してください．
			return (f[1] - f[0]) / h;
		}
	}

	//
	// 中心差分値計算メソッド
	//  【入力】
	//   h = 差分幅
	//   f = 関数値格納配列 (f[0] = f(a - h), f[1] = f(a + h))
	//
	//  【返却値】
	//   a における中心差分値
	//
	// * 第7回講義スライド4ページに掲載されている中心差分による
	//   数値微分メソッドです．
	//
	double CentralDifference(double h, double[] f)
	{
		if (h <= Double.MIN_VALUE){
			throw new IllegalArgumentException("分割幅が小さすぎます．");
		}
		else if (f.length != 2){
			throw new IllegalArgumentException("配列サイズは 2 にして下さい．");
		}
		else{
			// [演習] 講義スライドp.3を参考にして中心差分近似のプログラムを作成してください．
			return (f[1] - f[0]) / (2 * h);
		}
	}
}
