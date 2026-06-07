//
// 【FFTクラス】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class FFT {
		
	//
	// FFT計算メソッド
	//
	//
	// 【入力】
	//   M = サンプリング数の指数（N = 2^M）
	//   f = サンプリング値
	//
	// 【返却値】
	//   2次元配列（FFT結果を格納．c[j][0]に実部，c[j][1]に虚部を格納）
	//
	double[][] calc(int M, double[] f){
		int j, p, lp, q, l, lq, k;
		int rev, N = (int)Math.pow(2,M);
		double cr, ci;
		
		// DFT結果格納配列
		double[][] c = new double[N][2];
		
		// 回転因子
		double theta, wq_r, wq_i;
		
		// [演習] FFTメソッドを完成させてください．
		
		// ビットリバースによる並び替え
		int i = 0;
		double tmpFFT_r, tmpFFT_i;
    	for (j = 1; j < N - 1; j++) {
        	for (k = N >> 1; k > (i ^= k); k >>= 1);
        	if (j < i) {
				tmpFFT_r = c[j][0];
				tmpFFT_i = c[j][1];
				c[j][0] = c[i][0];
				c[j][1] = c[i][1];
				c[i][0] = tmpFFT_r;
				c[i][1] = tmpFFT_i;
        	}
    	}
		
		return(c);
	}
	
	//
	// IFFT計算メソッド
	//
	// 【入力】
	//   M = サンプリング数の指数（N = 2^M）
	//   c = FFT格納配列（2^M行2列配列）
	//
	// 【返却値】
	//   サンプリング値（2次元配列：f[j][0]に実部，f[j][1]に虚部を格納）
	//
	double[][] icalc(int M, double[][] c){
		int j, p, pl, q, l, lq, k;
		int rev, N = (int)Math.pow(2,M);
		double fr, fi;
		
		// サンプリング値結果格納配列
		double[][] f = new double[N][2];
		
		// 回転因子
		double theta, wq_r, wq_i;
		
		// [演習] IFFTメソッドを完成させてください．
		
		// ビットリバースによる並び替え
		int i = 0;
		double tmpIFFT_r, tmpIFFT_i;
    	for (j = 1; j < N - 1; j++) {
        	for (k = N >> 1; k > (i ^= k); k >>= 1);
        	if (j < i) {
				tmpIFFT_r = f[j][0];
				tmpIFFT_i = f[j][1];
				f[j][0] = f[i][0];
				f[j][1] = f[i][1];
				f[i][0] = tmpIFFT_r;
				f[i][1] = tmpIFFT_i;
        	}
    	}
		
		return(f);
	}
}

