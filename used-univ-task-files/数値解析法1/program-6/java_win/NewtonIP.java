import java.util.Arrays;
//
// 【ニュートンの補間公式クラス  NewtonIP】
//
//  同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
//  利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class NewtonIP{
    int n; 			// 補間多項式の次数
    double [] x; 	// 分点
    double [] c;	// 係数

    // コンストラクタ
    // 【入力】
    //   xp[] = 分点座標
    //   f[] = 分点における関数値
    //
    //   * 講義使用スライド15ページのアルゴリズムにより係数計算
    //
    NewtonIP(double [] xp, double [] f){
		int i, j;
		
		n = xp.length - 1;
		
		// 配列 xp, f のデータコピー (ディープコピー)
		x = Arrays.copyOf(xp, n + 1);
		c = Arrays.copyOf(f, n + 1); // c_i = f (x_i) (i = 0,1,...,n)

		// [演習] ニュートンの補間公式係数計算アルゴリズム (p.15) を実装してください
    }

    //
    //  ニュートンの補間公式による補間値計算メソッド
    //  【入力】
    //     xval = 補間値を求める点の座標値 (スライドのxをxvalに読み替えてください)
    //
    //  【返却値】
    //     xval における関数の補間値
    //
    double Newton_interp(double xval)
    {
		if (xval < x[0] || xval > x[n]){
	    	throw new IllegalArgumentException("補間値を求める点が不正です．");
		}
		else{
			// [演習] ホーナーの算法 (p.16) による補間値計算アルゴリズムを実装してください
		}
    }
}
