import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
//
// 【逐次反復法クラス  SI】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class SI {
	static int ITERMAX = 1000;	// 最大反復回数
	// 絶対許容誤差 (IEEE754倍精度の最小有限値)
	double epsa = Double.MIN_VALUE;
	// 相対許容誤差 (IEEE754倍精度計算機イプシロン X 8.0D)
	double epsr = 8.0D * Math.ulp(1.0D);

	// 例題の解
   	double[] exactx = {0.5D, 0.25D}; // 13ページ

	//
	// 非線形方程式に対する逐次反復法メソッド (ベクトル用)
	//  【入力】
	//   x0 = 初期ベクトル
	//
	//  【返却値】
	//   f(x) = 0 の近似解ベクトル
	//
	double[] solve(double[] x0) {
		int i, n = x0.length, k = 0;
		double oldnorm, newnorm, sumnorm;
		double error, errval, olderrval;

		double [] x = Arrays.copyOf(x0, n);
		double [] xnew = new double[n];
		double [] fvec = new double[n];
		double [] errvec = new double[n];
			
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-si2d.dat"))))){
			errvec = UtilityLA.vecpm(x, exactx, 'm');
			olderrval = UtilityLA.norm(errvec, 0);

			// 初期ベクトルの最大値ノルム値計算
			oldnorm = UtilityLA.norm(x, 0);

			// 逐次反復
			do{
				fvec = f(x);
				xnew = UtilityLA.vecpm(x, fvec, 'm');
				newnorm = UtilityLA.norm(xnew, 0);
				sumnorm = oldnorm + newnorm;
				oldnorm = newnorm;
				error = UtilityLA.norm(fvec, 0);
				x = Arrays.copyOf(xnew,n);

				k++;

				pw.printf("%4d", k);
				for (i = 0 ; i < n ;i++){
					pw.printf("  %17.15e", x[i]);
				}
				
				errvec = UtilityLA.vecpm(x, exactx, 'm');
				errval = UtilityLA.norm(errvec, 0);
				//pw.printf("  %17.15e  %17.15e\n", Math.log(errval), errval / olderrval);
				pw.printf("  %17.15e  %17.15e\n", errval, errval / olderrval);
				
				olderrval = errval;
			}while (error >= epsa + epsr * sumnorm && k < ITERMAX);

			// 出力ファイルストリームのクローズ
			pw.close();
		}
		catch(IOException e){
			System.err.println(e);
		}

		if (k >= ITERMAX){
			System.out.println("Not converged ...");
		}
		else{
		  	System.out.printf("Converged：num. of iterations = %d\n",k);
		}

		return(x);
	}

	// 例題 (13ページ) のベクトル値関数 f (x)
	double[] f(double[] x){
		double [] val = new double[x.length];

		val[0] = 0.5D * x[0] * x[0] + x[0] + 0.25D * x[1] - 11.0D/16.0D;
		val[1] = 0.25D * x[0] + 0.5D * x[1] * x[1] + x[1] - 13.0D / 32.0D;

		return(val);
	}
}
