import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
// コンパイル: javac UseFFT.java
// 実行: java UseFFT
//
// として下さい．
//
// [実行例]
// Calculation time (FFT) = 0.002694 [s]
// Abs. error (IFFT) = 8.881784197001252e-16
// 注) 実行環境などにより計算環境は異なります．
//
class UseFFT {
	public static void main(String[] args) {
		int j;
		double ifft_err = 0.0D, abserr;
		
		// サンプリング数
		int k = 9;
		int N = (int)Math.pow(2,k);
		
		// サンプリング間隔
		double T = 4.0D;
		double dt = T / N;
		
		// サンプリング値格納配列
		double[] f = new double[N];
		
		// FFT格納配列
		double[][] c = new double[N][2];
		
		// IFFT格納配列
		double[][] ifft_f = new double[N][2];
		
		// サンプリング値の格納
		for (j = 0; j < N; j++){
			f[j] = funcf(j * dt);
		}
		
		// FFT計算オブジェクトの作成
		FFT fft = new FFT();
		
		// FFT計算
		long start = System.nanoTime();
		c = fft.calc(k,f);
		long end = System.nanoTime();
		double ctime = (double)(end - start);
		System.out.println("Calculation time (FFT) = "+ctime/100000000+" [s]");
		
		// IFFT計算
		ifft_f = fft.icalc(k, c);
		
		try{
			// 出力ファイルストリームオープン
			File ofile_dft = new File("exc-fft.dat");
			PrintWriter pw_dft = new PrintWriter(new BufferedWriter(new FileWriter(ofile_dft)));
			
			// 結果のファイルへの出力
			for (j = 0; j < N; j++){
				pw_dft.printf("%17.15e  %17.15e  %17.15e  %17.15e\n",j/T,c[j][0],c[j][1],Math.sqrt(c[j][0]*c[j][0]+c[j][1]*c[j][1])/Math.sqrt(N));
				abserr = Math.abs(ifft_f[j][0] - f[j]);
				if (ifft_err < abserr) ifft_err = abserr;
			}
			
			pw_dft.close();
			System.out.printf("Abs. error (IFFT) = %17.15e\n",ifft_err);
		}
		catch (IOException e){
			System.out.println(e);
		}
	}
	

	// 例題関数
	static double funcf(double t){
		double fval, freq1=20.0D, freq2 = 40.0D;
		
		if (t >= 0.0D && t <= 4.0D){
			fval = Math.sin(2*Math.PI*freq1*t) + 0.25D*Math.sin(2*Math.PI*freq2*t);
		}
		else{
			fval = 0.0D;
		}
		
		return(fval);
	}
}