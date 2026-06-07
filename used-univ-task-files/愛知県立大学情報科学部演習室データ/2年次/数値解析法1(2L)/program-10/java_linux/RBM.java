//
// 【ロンバーグ積分公式クラス  RBM】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class RBM {

	//
	// ロンバーク積分法による積分計算
	//  【入力】
	//    k = 分割最大数-1
	//    a,b = 積分区間
	//
	//  【返却値】
	//    積分近似値の配列
	//
	//  * 第10回講義スライド31ページ掲載のロンバーグ積分法メソッドです．
	//
	double calc(int k, double a, double b)
	{
		int i, j, l, N = 2;
		double h;

		if (k < 0)
		{
			throw new IllegalArgumentException("分割最大数2^k N0のkには，正の値を入力して下さい．");
		}
		else if (b <= a)
		{
			throw new IllegalArgumentException("積分区間はa < bとなるように入力して下さい．");
		}
		else
		{
			// 複合台形公式の結果格納用配列
			double[] rom = new double[k+1];
				
			// 被積分関数値格納用配列
			double[] fval = new double[(int)(Math.pow(2,k+1)) + 1];
			
			// T表作成のための配列
			double[][] ttable = new double[k+1][k+1];
			
			try{
				// 計算結果出力ファイル
				File ofilet = new File("exc-rom-t-table.dat");
				PrintWriter pwt = new PrintWriter(new BufferedWriter(new FileWriter(ofilet)));
				
				// 被積分関数オブジェクトの生成
				ItgF f = new ItgF();
				
				// ニュートン・コーツ公式オブジェクトの生成
				NCF ncm = new NCF();
				
				// 複合台形公式による数値積分
				for (j = 0; j <= k; j++){
					h = (b - a) / N;
					for (i = 0 ; i <= N ; i++){
						fval[i] = f.value(a + i * h);
					}
					rom[j] = ncm.TrapezoidalRule(N, h, fval);
					ttable[j][0] = rom[j];
					N *= 2;
				}
			
				// [演習] ロンバーグ積分法メソッドを完成させてください
				for (j = 0; j <= k; j++){
					rom[j] = ttable[j][0];
				}
				int temp = 1;
				for (j = 1; j <= k; j++){
					temp *= 4;
					for (l = k - j ; l >= 0 ; l--){
						
						rom[l + j] += (rom[l + j] - rom[l + j - 1])/(temp - 1); 
					}
				}
				// T表の出力
				for (i = 0; i <= k; i++){
					for (j = 0; j <= i; j++){
						pwt.printf("%17.15e  ", ttable[i][j]);
					}
					pwt.printf("\n");
				}
				pwt.close();
			}
			catch (IOException e){
				System.out.println(e);
			}
			
			return(rom[k]);
		}
	}
}
