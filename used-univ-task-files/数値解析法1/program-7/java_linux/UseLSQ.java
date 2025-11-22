import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
//
// コンパイル: javac LSQ.java
// 実行: java UseLSQ
//
// として下さい．
//
// [実行結果]
//  m に 3 を入力した場合：
//最小二乗近似多項式の係数：
// 0 次の係数 = -1.520000000000976e+00
// 1 次の係数 = 1.871666666668008e+00
// 2 次の係数 = -4.625000000005016e-01
// 3 次の係数 = 5.583333333338825e-02
//
class UseLSQ {
	public static void main(String[] args) {
		int i, k;
		double appval;

		try
		{
			// データ点
			double [] x = {1.0D, 2.0D, 3.0D, 4.0D, 5.0D};
			// 分点におけるデータ値
			double [] f = {0.0D, 0.6D, 1.77D, 1.92D, 3.31D};

			// 最小二乗近似多項式次数の入力
			Scanner keyBoardScanner = new Scanner(System.in);
			System.out.println("最小二乗近似多項式の次数を入力して下さい:");
			System.out.print("m = ");
			int m = keyBoardScanner.nextInt();

			// 最小二乗近似多項式インスタンスの生成
			LSQ lsm = new LSQ(m);

			// 係数の計算
			int rval = lsm.calcCoef(x, f);

			if (rval == 0)
			{
				System.err.println("正規方程式を解いている段階で異常終了しました．");
			}
			else if (rval == -1)
			{
				System.err.println("正規方程式の係数行列は正則ではありません．");
			}
			else
			{
				// 最小二乗近似多項式の係数出力
				double[] c = new double[m + 1];
				lsm.showCoef(c);
				System.out.println("最小二乗近似多項式の係数：");
				for (i = 0 ; i <= m ; i++) {
					System.out.printf("%2d 次の係数 = %17.15e\n",i, c[i]);
				}

				// データの出力
				File dfile = new File("exc-xf.dat");
				PrintWriter dpw = new PrintWriter(new BufferedWriter(new FileWriter(dfile)));
				for (k = 0 ; k < x.length ; k++) {
					dpw.printf("%17.15e %17.15e\n", x[k], f[k]);
				}
				dpw.close();

				// 出力ファイルストリームオープン
				File ofile = new File("exc-lsq.dat");
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile)));

				// 最小二乗近似多項式の値の出力: [x[0], x[n]] より少し広い範囲を 200 等分割して出力
				double width = (x[x.length-1] - x[0]) / 200;
				double xval = x[0] - 10 * width;
				for (k = 0 ; k <= 220 ; k++)
				{
					appval = lsm.calcLSP(xval);
					pw.printf("%17.15e %17.15e\n", xval, appval);
					xval += width;
				}

				// 出力ファイルストリームのクローズ
				pw.close();
			}
		}
		catch(IOException e)
		{
		      System.err.println(e);
	    }
	}
}
