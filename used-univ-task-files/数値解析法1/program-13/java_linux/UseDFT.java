import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
// コンパイル: javac UseDFT.java
// 実行: java UseDFT
//
// として下さい．
//
// [実行例]
// Calculation time (DFT) = 0.066753 [s]
// Abs. error (IDFT) = 1.343004679022258e-13
// 注) 実行環境などにより計算時間は異なります．
//
class UseDFT {
	public static void main(String[] args) {
		int j;
		double idft_err = 0.0D, abserr;
		
		// サンプリング数
		int k = 9;
		int N = (int)Math.pow(2,k);
		
		// サンプリング間隔
		double T = 4.0D;
		double dt = T / N;
		
		// サンプリング値格納配列
		double[] f = new double[N];
		
		// DFT格納配列
		double[][] c = new double[N][2];
		
		// IDFT格納配列
		double[][] idft_f = new double[N][2];
		
		// サンプリング値の格納
		for (j = 0; j < N; j++){
			f[j] = funcf(j * dt);
		}
		
		// DFT計算オブジェクトの作成
		DFT dft = new DFT();
		
		// DFT計算
		long start = System.nanoTime();
		c = dft.calc(f);
		long end = System.nanoTime();
		double ctime = (double)(end - start);
		System.out.println("Calculation time (DFT) = "+ctime/100000000+" [s]");
		
		// IDFT計算
		idft_f = dft.icalc(c);
		
		try{
			// 出力ファイルストリームオープン
			File ofile_dft = new File("exc-dft.dat");
			PrintWriter pw_dft = new PrintWriter(new BufferedWriter(new FileWriter(ofile_dft)));
			
			// 結果のファイルへの出力
			for (j = 0; j < N; j++){
				pw_dft.printf("%17.15e  %17.15e  %17.15e  %17.15e\n",j/T,c[j][0],c[j][1],Math.sqrt(c[j][0]*c[j][0]+c[j][1]*c[j][1])/Math.sqrt(N));
				abserr = Math.abs(idft_f[j][0] - f[j]);
				if (abserr > 1.34e-13) {
					System.out.printf("Error at j=%d: %17.15e\n", j, abserr);
				}
				if (idft_err < abserr) idft_err = abserr;
			}
			
			pw_dft.close();
			System.out.printf("Abs. error (IDFT) = %17.15e\n",idft_err);
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