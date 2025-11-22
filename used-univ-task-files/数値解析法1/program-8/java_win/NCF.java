//
// 【複合ニュートン・コーツ型積分公式クラス  NCF】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class NCF {
	//
	// 複合台形公式 メソッド (等分割用)
	//  【入力】
	//   N = 分割数
	//   h = 分割幅
	//   f = 関数値が格納された配列
	//
	//  【返却値】
	//   複合台形公式による積分の近似値
	//
	// * 第7回講義スライド 25ページ掲載の複合台形公式メソッドです．
	//
	double TrapezoidalRule(int N, double h, double[] f){
		int i;

		if (N <= 0){
			throw new IllegalArgumentException("分割数が不正です．");
		}
		else if (h <= 0.0D){
			throw new IllegalArgumentException("分割幅は 0 より大きくとりましょう．");
		}
		else{
		  	// [演習] 複合台形公式メソッドを完成させてください．
		}
	}

	//
	// 複合シンプソン公式 メソッド (等分割)
	//  【入力】
	//   N = 分割数
	//   h = 分割幅
	//   f = 関数値が格納された配列
	//
	//  【返却値】
	//   複合シンプソン公式による積分の近似値
	//
	// * 第7回講義スライド 27ページ掲載の複合シンプソン公式メソッドです．
	//
	double SimpsonRule(int N, double h, double[] f){
		int i;
		
		if (N <= 0 || N % 2 != 0){
			throw new IllegalArgumentException("分割数が不正です．");
		}
		else if (h <= 0.0D){
			throw new IllegalArgumentException("分割幅は 0 より大きくとりましょう．");
		}
		else{
			// [演習] 複合シンプソン公式メソッドを完成させてください．
		}
	}

	//
	// 複合中点公式 メソッド (等分割)
	//  【入力】
	//   N = 分割数
	//   h = 分割幅
	//   f = 関数値が格納された配列
	//
	//  【返却値】
	//   複合中点公式による積分の近似値
	//
	// * 第7回講義スライド 26ページ掲載の複合中点公式メソッドです．
	//
	double MidpointRule(int N, double h, double[] f)
	{
		int i;

		if (N <= 0 || N % 2 != 0){
			throw new IllegalArgumentException("分割数が不正です．");
		}
		else if (h <= 0.0D){
			throw new IllegalArgumentException("分割幅は 0 より大きくとりましょう．");
		}
		else{
		  	// [演習] 複合中点公式メソッドを完成させてください．
		}
	}
}
