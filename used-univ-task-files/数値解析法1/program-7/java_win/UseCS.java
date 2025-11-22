//
// コンパイル: javac UseCS.java
// 実行: java UseCS
//
// として下さい．
//
// [実行結果]
// 最大絶対誤差 = 8.918221615081906e-03
//
// 第6回講義スライド19または20ページを参考にグラフを描画し，22ページと同様のグラフになるか確認してください．
// なお，出力されるファイル名は exc-cs.dat です．
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
class UseCS{
	public static void main(String[] args) {
		int i, k;
		double maxerr = 0.0D, appval, errval;

		// 区間
		double a = -1.0D, b = 1.0D;
		// 分点数 n (分点の数は n + 1)
		int n = 16;
		// 分点
		double [] x = new double[n + 1];
		// 分点における関数値
		double [] fval = new double[n + 1];
		// 端点での傾き
		double [] dfval = new double[2];
		// 分割の幅 (等分割)
		double h = (b - a) / n;

		try
		{
			// 3次スプライン補間オブジェクトの生成
			CSpline cs = new CSpline();

			// 分点および関数の計算
			for (i = 0 ; i <= n ; i++)
			{
				x[i] = a + i * h;
				fval[i] = f(x[i]);
			}
			dfval[0] = 25.0D/338.0D; dfval[1] = -25.0D/338.0D;

			// 係数の計算
			cs.SplineCoef(x, fval, dfval);

			// 出力ファイルストリームオープン
			File ofile = new File("exc-cs.dat");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile)));

			// 補間値, 絶対誤差の出力: [x[0], x[n]] を 200 等分割して出力
			double xval = x[0];
			double width = (x[n] - x[0]) / 200;
			for (k = 0 ; k <= 200 ; k++)
			{
				appval = cs.Interpolation(xval);
				errval = Math.abs(appval - f(xval));
				pw.printf("%17.15e %17.15e %17.15e\n", xval, appval, errval);
				if (maxerr < errval) maxerr = errval;
				xval += width;
				if (xval > x[n]) xval = x[n];
			}

			// 出力ファイルストリームのクローズ
			pw.close();

			// 最大相対誤差の出力
			System.out.printf("\n最大絶対誤差 = %17.15e\n\n", maxerr);
		}
		catch(IOException e)
		{
		      System.err.println(e);
	    }
	}

	// 例題関数 (ルンゲの現象)
	static double f(double x)
	{
		return(1.0D / (1.0D + 25.0D * x * x));
	}
}
