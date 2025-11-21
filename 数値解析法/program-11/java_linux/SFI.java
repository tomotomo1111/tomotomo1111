import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
// 【ステファンセン反復クラス  SfI】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class SFI {
	static int ITERMAX = 1000;	// 最大反復回数
	// 絶対許容誤差 (IEEE754倍精度の最小有限値)
	double epsa = Double.MIN_VALUE;
	// 相対許容誤差 (IEEE754倍精度計算機イプシロン X 8.0D)
	double epsr = 8.0D * Math.ulp(1.0D);

	// 例題の解
	double exactx = 0.5D;

	//
	// 非線形方程式に対するステファンセン反復メソッド
	//  【入力】
	//   x0 = 初期値
	//
	//  【返却値】
	//   f(x) = 0 の近似解
	//
	double solve(double x0)
	{
		int k = 0;
		double x = x0, xnew = 0.0D;
		double sumnorm, oldnorm, newnorm;
		double error, errval, olderrval;
		double g1, g2;
		error = sumnorm =  0;

		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter( new File("exc-sfi.dat"))))){
			errval = Math.abs(x - exactx);
			olderrval = errval;
			oldnorm = Math.abs(x);

			// ステファンセン反復
			do{
				// [演習] ステファンセン反復による求解メソッドを完成させてください
				g1 = g(x);
				g2 = g(g1);
				xnew = x - Math.pow(g1 - x, 2) / (g2 - 2 * g1 + x);
				sumnorm = Math.abs(x) + Math.abs(xnew);
				error = Math.abs(xnew - x);
				x = xnew;

				k++;

				errval = Math.abs(xnew - exactx);
				pw.printf("%4d  %17.15e  %17.15e  %17.15e\n", k, xnew, errval, errval / olderrval);
				olderrval = errval;

			} while (error >= epsa + epsr * sumnorm && k < ITERMAX);

			pw.close();
		}
		catch(IOException e){
		      System.err.println(e);
	    }

		if (k == ITERMAX){
			System.out.println("Not converged ...");
		}
		else
		{
			System.out.printf("Converged (Steffensen): num. of iterations = %d\n",k);
		}
			
		return(x);
	}

	// 例題の関数 f
	double f(double x) {
		return(Math.pow(x, 5)/24.0D - 13.0D*Math.pow(x, 4)/48.0D + 17.0D*Math.pow(x, 3)/24.0D - 23.0D*Math.pow(x, 2)/24.0D + 2.0D*x/3.0D - 1.0D/6.0D);
	}

	// 同値変形関数
	double g(double x) {
		return(x - f(x));
	}
}
