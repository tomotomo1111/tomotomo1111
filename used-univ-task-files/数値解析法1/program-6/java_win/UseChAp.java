//
// コンパイル: javac UseChAp.java
// 実行: java UseChAp
//
// として下さい．
//
//  [実行結果]
// 最大絶対誤差 = 3.260398454064872e-02
//
// スライド19または20ページを参考にグラフを描画し，37ページと同様のグラフになるか確認してください．
// なお，出力されるファイル名は exc-ip-cheby.dat です．
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
class UseChAp{
    public static void main(String[] args) {
	int i, k;
	double maxerr = 0.0D, appval, errval;

	// 多項式の次数
	int n = 16;
	// 分点
	double [] x = new double[n + 1];
	// 分点における関数値
	double [] fval = new double[n + 1];

	try{
	    // チェビシェフ近似オブジェクトの生成
	    ChebyApp ca = new ChebyApp(n);

	    // 分点の取得
	    ca.showpoints(x);

	    // 分点および関数の計算
	    for (i = 0 ; i <= n ; i++) fval[i] = f(x[i]);

	    // 係数の計算
	    ca.ChebyCoef(fval);

	    // 出力ファイルストリームオープン
	    File ofile = new File("exc-ip-cheby.dat");
	    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile)));

	    // 補間値, 絶対誤差の出力: [x[0], x[n]] を 200 等分割して出力
	    double xval = x[0];
	    double width = (x[n] - x[0]) / 200;
	    for (k = 0 ; k <= 200 ; k++){
			appval = ca.ChebyCalc(xval);
			errval = Math.abs(appval - f(xval));
			pw.printf("%17.15e %17.15e %17.15e\n", xval, appval, errval);
			if (maxerr < errval) maxerr = errval;
			xval += width;
	    }

	    // 出力ファイルストリームのクローズ
	    pw.close();

	    // 最大絶対誤差の出力
	    System.out.printf("最大絶対誤差 = %17.15e\n", maxerr);
	}
	catch(IOException e){
	    System.err.println(e);
	}
    }

    // ルンゲの例題
    static double f(double x){
	return(1.0D / (1.0D + 25.0D * x * x));
    }
}
