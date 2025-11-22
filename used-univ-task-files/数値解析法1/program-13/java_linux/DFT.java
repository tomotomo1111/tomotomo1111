//
// 【DFTクラス】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class DFT {
	
	//
	// DFT計算メソッド
	//
	// 【入力】
	//   f = サンプリング値
	//
	// 【返却値】
	//   2次元配列（DFT結果を格納．c[j][0]に実部，c[j][1]に虚部を格納）
	//
	double[][] calc(double[] f){
		int /*j, k,*/ N = f.length;
		
		// DFT結果格納配列
		double[][] c = new double[N][2];
		
		// 回転因子角度 2 pi/N
		double theta = 2 * Math.PI / N;
		
		// [演習] DFTメソッドを完成させてください
		for (int j = 0; j < c.length; j++) {
			for (int i = 0; i < c[0].length; i++) c[j][i] = 0;
		}
		for (int j = 0; j < N; j++) {
			for (int i = 0; i < N; i++) {
				c[j][0] += f[i] * Math.cos(theta * i * j);
				c[j][1] -= f[i] * Math.sin(theta * i * j);
			}
		}
		return(c);
	}
	
	//
	// IDFT計算メソッド
	//
	// 【入力】
	//   c = DFT格納配列（N行2列配列）
	//
	// 【返却値】
	//   サンプリング値（2次元配列：f[j][0]に実部，f[j][1]に虚部を格納）
	//
	double[][] icalc(double[][] c){
		int /*j, k,*/ N = c.length;
		
		// IDFT結果格納配列
		double[][] f = new double[N][2];
		
		// 回転因子角度 2 pi / N
		double theta = 2 * Math.PI / N;
		
		// [演習] IDFTメソッドを完成させてください
		for (int j = 0; j < f.length; j++) {
			for (int i = 0; i < f[0].length; i++) f[j][i] = 0;
		}

		for (int j = 0; j < N; j++) {
			for (int i = 0; i < N; i++) {
				f[j][0] += ((c[i][0] * Math.cos(theta * i * j) - c[i][1] * Math.sin(theta * i * j)) / N);
				f[j][1] += ((c[i][0] * Math.sin(theta * i * j) + c[i][1] * Math.cos(theta * i * j)) / N);
			}
		}
		return(f);
	}
}
