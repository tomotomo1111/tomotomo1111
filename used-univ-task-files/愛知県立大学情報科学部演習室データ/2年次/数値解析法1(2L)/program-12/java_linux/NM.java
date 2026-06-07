import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
//
// 【Newton 法クラス  NM】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class NM {
	static int ITERMAX = 1000;	// 最大反復回数
	// 絶対許容誤差 (IEEE754倍精度の最小有限値)
	double epsa = Double.MIN_VALUE;
	// 相対許容誤差 (IEEE754倍精度計算機イプシロン X 8.0D)
	double epsr = 8.0D * Math.ulp(1.0D);

	// 例題 (6ページ, 17ページ) の解
	double exactval = 0.5D;

	// 例題 (13ページ) の解ベクトル
	double[] exactx = {0.5D, 0.25D};

	//
	// 1次元非線形方程式に対するニュートン法
	//  【入力】
	//   x0 = 初期値
	//
	//  【返却値】
	//   f(x) = 0 の近似解
	//
	// * スカラー値関数用．後半第2回使用スライド 5 ページ掲載方法を実装．
	//   f (x), f'(x) は，同一クラス内に定義されています．
	//
	double solve(double x0)
	{
		int k = 0;
		double xnew = 0.0D, x = x0, dfx;
		double sumnorm, oldnorm, newnorm;
		sumnorm = oldnorm = newnorm = 0;
		double olderrval, errval, error;
		olderrval = errval = error = 0;

		olderrval = Math.abs(x - exactval);
		oldnorm = Math.abs(x);

		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-nm1d.dat"))))){
			// gnuplot用
			pw.printf("%4d  %17.15e  %17.15e  %17.15e\n", k, x, olderrval, Math.log(olderrval));

			do{
				dfx = df(x);
				if (Math.abs(dfx) <= epsa){
				       System.out.println("微分係数値が非常に小さくなりました．終了します．");
				       break;
				}
				
				// [演習]1次元非線形方程式に対するニュートン法による求解メソッドを完成させてください．
				xnew = x - (f(x) / dfx);
				x = xnew;
				newnorm = Math.abs(x);
				sumnorm = oldnorm + newnorm;
				oldnorm = newnorm;
				
				k++;
				errval = Math.abs(xnew - exactval);
				error = errval;
				//pw.printf("%4d  %17.15e  %17.15e  %17.15e\n", k, xnew, errval, errval / olderrval);
				pw.printf("%4d  %17.15e  %17.15e  %17.15e  %17.15e\n", k, xnew, errval, Math.log(errval), errval / olderrval); // gnuplot用
				olderrval = errval;				
			} while (error >= epsa + epsr * sumnorm && k < ITERMAX);

			// 出力ファイルストリームのクローズ
			pw.close();
		}
		catch(IOException e){
			System.err.println(e);
	    }

		if (k == ITERMAX){
			System.out.println("Not converged ...");
		}
		else{
		    System.out.printf("Converged: num. of iterations = %d\n",k);
		}
		
		return(xnew);
	}

	// 例題の関数 f
	double f(double x) {
		return(Math.pow(x, 5)/24.0D - 13.0D*Math.pow(x, 4)/48.0D + 17.0D*Math.pow(x, 3)/24.0D - 23.0D*Math.pow(x, 2)/24.0D + 2.0D*x/3.0D - 1.0D/6.0D);
	}

	// 導関数 f'(x)
	double df(double x) {
		return(5.0D * Math.pow(x, 4)/24.0D - 13.0D*Math.pow(x, 3)/12.0D + 17.0D*Math.pow(x, 2)/8.0D - 23.0D * x /12.0D + 2.0D/3.0D);
	}

    //
	// 多次元非線形方程式に対するニュートン法 (オーバーロード)
	//  【入力】
	//   x0 = 初期ベクトル (1次元配列)
	//
	//  【返却値】
	//   f(x) = 0 の近似ベクトル
	//
	// * ベクトル値関数用． f(x) およびヤコビ行列は，同一クラス内に定義されています．
	// * Gausseliminationクラスによりベクトルhを求めています．
	//
	double[] solve(double[] x0)
	{
		int i, k = 0, n = x0.length, grslt = 1;
		double[] x = Arrays.copyOf(x0, n);
		double[] xnew = new double[n];
		double[] h = new double[n];
		double oldnorm, newnorm, sumnorm;
		double errval, olderrval, error;
		double[] errvec = new double[n];
		error = 0;
		oldnorm = UtilityLA.norm(x,0);
		errvec = UtilityLA.vecpm(exactx, x, 'm');
		olderrval = UtilityLA.norm(errvec, 0);
	
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("exc-nm2d.dat"))))){
			// gnuplot用
			//pw.printf("%4d",k);
			//for (i = 0 ; i < n ; i++) pw.printf("  %17.15e", x[i]);
			//pw.printf("  %17.15e  %17.15e\n", olderrval, Math.log(olderrval));

			// ガウスの消去法クラスのインスタンス生成
			GaussEl ge = new GaussEl();

			do {
				h = f(x);
				for (i = 0 ; i < n ; i++) h[i] *= -1.0D;
				grslt = ge.getSol(Jacobif(x), h);
				if (grslt == 0 || grslt == -1){
					break;
				}

				// [演習] 多次元非線形方程式に対するニュートン法による求解メソッドを完成させてください．
				do {
					for (i = 0; i < n ; i++){
						xnew[i] = x[i] + h[i];
					}
					newnorm = UtilityLA.norm(xnew, 0);
					sumnorm = oldnorm + newnorm;
					oldnorm = newnorm;
					x = xnew;	
				} while (UtilityLA.norm(h, 0) < epsa + epsr * sumnorm);

				k++;

				// ベクトルの出力および誤差の計算
				pw.printf("%4d",k);
				for (i = 0 ; i < n ; i++) pw.printf("  %17.15e", xnew[i]);
				errvec = UtilityLA.vecpm(xnew, exactx, 'm');
				errval = UtilityLA.norm(errvec, 0);
				error = errval;
				pw.printf("  %17.15e  %17.15e\n", errval, errval / olderrval);
				//pw.printf("  %17.15e  %17.15e  %17.15e\n", errval, log(errval), errval / olderrval); // gnuplot用
				olderrval = errval;
			} while (error >= epsa + epsr * sumnorm && k < ITERMAX);

			// 出力ファイルストリームのクローズ
			pw.close();
		} catch(IOException e){
			System.err.println(e);
	    }

		if (k == ITERMAX){
			System.out.println("Not converged");
		}
		else if (grslt == 0){
			System.out.printf("ERROR: fail to calculate the vector h at %d step.",k);
		}
		else if (grslt == -1){
			System.out.printf("ERROR: Jacobi matrix at %d step is singular.",k);
		}
		else{
		    System.out.printf("Converged: num. of iterations = %d\n",k);
		}

		return(x);
	}

	// 例題のベクトル値関数 f (オーバーロード)
	double[] f(double[] x)
	{
		double[] rvec = new double[x.length];

		rvec[0] = Math.pow(x[0],2) / 2.0D + x[0] + x[1] / 4.0D - 11.0D / 16.0D;
		rvec[1] = x[0] / 4.0D + Math.pow(x[1],2) / 2.0D + x[1] - 13.0D / 32.0D;

		return(rvec);
	}

	// ヤコビ行列
	double[][] Jacobif(double[] x)
	{
		double[][] rmat = new double[x.length][x.length];

		rmat[0][0] = x[0] + 1.0D;
		rmat[0][1] = 0.25D;
		rmat[1][0] = 0.25D;
		rmat[1][1] = x[1] + 1.0D;

		return(rmat);
	}
}
