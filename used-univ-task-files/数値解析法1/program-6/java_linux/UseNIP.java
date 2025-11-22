//
// コンパイル: javac UseNIP.java
// 実行: java UseNIP
//
// として下さい．
//
// [実行結果（cosineの場合）]
// 最大絶対誤差 = 1.136988281302820e-10
//
// スライド19または20ページを参考にグラフを描画し，18ページと同じグラフになるか確認してください．
// また，ルンゲの例題についても実行し，結果とグラフを確認してください．
//
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
class UseNIP{
    public static void main(String[] args){
		int i, k;
		double maxerr = 0.0D, nival, errval;
		
		// 区間
		double a = -Math.PI, b = Math.PI;
		//double a = -1.0D, b = 1.0D; // ルンゲの例題

		// 分割数
		int n = 16;
		// 分割幅
		double h = (b - a) / n;
		// 分点
		double [] x = new double[n + 1];
		// 分点における関数値
		double [] fval = new double[n + 1];

		// 分点および関数の計算
		for (i = 0 ; i <= n ; i++){
		    x[i] = a + i * h;
		    fval[i] = f(x[i]);
		}

		// ニュートンの補間公式オブジェクトの生成
		NewtonIP ni = new NewtonIP(x, fval);

		try{
		    // 出力ファイルストリームオープン
	   		File ofile = new File("exc-ip.dat");
	    	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile)));

	    	// 補間値, 絶対誤差の出力: [x[0], x[n]] を 200 等分割して出力
	    	double xval = x[0];
	    	double width = (x[n] - x[0]) / 200;
	    	for (k = 0 ; k <= 200 ; k++){
				nival = ni.Newton_interp(xval);
				errval = Math.abs(nival - f(xval));
				pw.printf("%17.15e %17.15e %17.15e\n", xval, nival, errval);
				if (maxerr < errval) maxerr = errval;
				xval += width;
	    	}

	    	// 出力ファイルストリームのクローズ
	    	pw.close();

	    	// 最大相対誤差の出力
	    	System.out.printf("最大絶対誤差 = %17.15e\n", maxerr);
		}
		catch(IOException e){
	    	System.out.println(e);
		}
    }

    static double f(double x){
		// 例題 (cosine)
		return(Math.cos(x));
		// ルンゲの例題
		//return(1.0D/(1.0D+25.0D*x*x));
    }
}
