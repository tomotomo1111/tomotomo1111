import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
// 【逐次反復＋エイトケン加速クラス  AkI】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
public class AKI{
	static int ITERMAX = 1000;	// 最大反復回数
	// 絶対許容誤差 (IEEE754倍精度の最小有限値)
	double epsa = Double.MIN_VALUE;
	// 相対許容誤差 (IEEE754倍精度計算機イプシロン X 8.0D)
	double epsr = 8.0D * Math.ulp(1.0D);

	// 例題の解
    double exactval = 0.5D; // 22ページ

	//
	// 非線形方程式に対する逐次反復法＋エイトケン加速メソッド
	//  【入力】
	//   x0 = 初期値
	//   flag_acc = エイトケン加速フラグ（true -> エイトケン加速あり，false -> エイトケン加速なし）
	//
	//  【返却値】
	//   f(x) = 0 の近似解
	//
	double solve(double x0, boolean flag_acc){
		int k = 0;
		double y = 0.0D, ynew;
		double sumnorm, oldnorm, newnorm;
		double error, errval, olderrval;

		double[] x = new double[3];

		// 出力ファイル名の定義
		String ofname;
		if (flag_acc == false){
			ofname = "exc-si.dat";
		}
		else
		{
			ofname = "exc-aki.dat";
		}
			
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(ofname))))){
			if (flag_acc == false){
				// 逐次近似法のみ
				y = x0;
			}
			else{
		    	// エイトケン加速による初項を計算
				x[0] = x0; x[1] = x[0] - f(x[0]); x[2] = x[1] - f(x[1]);
				y = x[0] - (x[1] - x[0]) * (x[1] - x[0]) / (x[2] - 2 * x[1] + x[0]);
			}

			olderrval = Math.abs(y - exactval);
			oldnorm = Math.abs(y);

			// 逐次反復法のみ
			if (flag_acc == false){
				do{
					ynew =  y - f(y);
					newnorm = Math.abs(ynew);
					sumnorm = oldnorm + newnorm;
					oldnorm = newnorm;
					error = Math.abs(ynew - y);
					y = ynew;
					
					k++;
					
					errval = Math.abs(ynew - exactval);
					pw.printf("%4d  %17.15e  %17.15e  %17.15e\n", k, ynew, errval, errval / olderrval);
					olderrval = errval;
				} while (error >= epsa + epsr * sumnorm && k < ITERMAX);
			} // 逐次反復＋エイトケン加速
			else{
				do{
					// [演習] エイトケン加速による求解メソッドを完成させてください

					k++;
					
					errval = Math.abs(ynew - exactval);
					pw.printf("%4d  %17.15e  %17.15e  %17.15e\n", k, ynew, errval, errval / olderrval);
					olderrval = errval;
				} while (error >= epsa + epsr * sumnorm && k < ITERMAX);
			}

			// 出力ファイルストリームのクローズ
			pw.close();
		}
		catch(IOException e){
			System.err.println(e);
		}

		if (k >= ITERMAX){
			System.out.println("Not converged");
		}
		else{
			if (flag_acc == false)
			{
				System.out.printf("Converged（simple iteration): num. of iterations = %d\n",k);
			}
			else
			{
				System.out.printf("Converged（simple + Aitken): num. of iterations = %d\n",k);				
			}
		}

		return(y);
	}

	// 例題 (22ページ) の関数 f
	double f(double x)
	{
		return(Math.pow(x, 5)/24.0D - 13.0D*Math.pow(x, 4)/48.0D + 17.0D*Math.pow(x, 3)/24.0D - 23.0D*Math.pow(x, 2)/24.0D + 2.0D*x/3.0D - 1.0D/6.0D);
	}
}
