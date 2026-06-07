//
// 【定常反復法クラス  LAIter】
//
// 同一ディレクトリ内の Java プログラムからは，オブジェクトを生成することで
// 利用できます．クラス内メソッドの仕様については，コメントを参照して下さい．
//  なおベクトル関連の計算には，UtilityLA クラスに定義されているメソッド
// (static メソッド) を使用しています． UtilityLA.java（完成品） を
// 同一ディレクトリ内に置いておいて下さい．
//
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
public class LAIter {
    // 最大反復回数
    int IRMAX = 5000;
    // 絶対許容誤差
    double epsa = Double.MIN_VALUE;
    // 相対許容誤差 (マシンイプシロン)
    double epsr = Math.ulp(1.0D);

    //
    // ヤコビ法メソッド
    //  【入力】
    //   a = 係数行列 (2次元配列)
    //   b = 右辺ベクトル
    //   x = 初期ベクトル
    //
    //  【出力】 (実行後)
    //   x = 解ベクトル
    //
    void JacobiMethod(double[][] a, double[] b, double[] x){
	int i, j, k = 0;
	
	try{
	    // 収束履歴ファイル
	    FileWriter file = new FileWriter("jacobi.dat");
	    PrintWriter pw = new PrintWriter(new BufferedWriter(file));

	    // 行列次元の取得
	    int n = a.length;

	    if(n == 0){
		throw new IllegalArgumentException("配列を使用して下さい．");
	    }
	    else if(a[0].length != n || b.length != n || x.length != n){
		throw new IllegalArgumentException("配列サイズが一致しません．");
	    }
	    else{
		double err = 0.0D;
		double oldnorm, newnorm, sumnorm;
		double [] newx = new double[n];
		double [] errvec = new double[n];

	    	oldnorm = UtilityLA.norm(x, 2);
		do{
		    for(i = 0 ; i < n ; i++){
			newx[i] = b[i];
			for(j = 0 ; j < n ; j++){
			    if (j != i) newx[i] -= a[i][j] * x[j];
			}
			newx[i] /= a[i][i];
		    }

		    // 更新ベクトルとノルム値計算
		    newnorm = UtilityLA.norm(newx, 2);
		    // ベクトルノルム値の和
		    sumnorm = oldnorm + newnorm;

		    // ベクトル間の差を計算
		    errvec = UtilityLA.vecpm(newx, x, 'm');
		    err = UtilityLA.norm(errvec, 2);

		    for(i = 0 ; i < n ; i++){
			x[i] = newx[i];
		    }
		    oldnorm = newnorm;

		    // 反復回数の更新・確認と収束履歴出力
		    k++;
		    pw.printf("%4d  %17.15e%n",k,err);
		    if(k == IRMAX){
			System.err.println("最大反復回数に到達しました．終了します．");
			pw.close();
			return;
		    }
		} while (err >= epsa + epsr * sumnorm);

		System.out.println("反復回数 (ヤコビ法) = "+k);
		pw.close();
	    }
	}
	catch(Exception e){
	    System.err.println(e);
	}
    }

    //
    // ガウス・ザイデル法メソッド
    //  【入力】
    //   a = 係数行列 (2次元配列)
    //   b = 右辺ベクトル
    //   x = 初期ベクトル
    //
    //  【出力】 (実行後)
    //   x = 解ベクトル
    //
    void GaussSeidelMethod(double[][] a, double[] b, double[] x){
	int i, j, k = 0;

	try{
	    // 収束履歴ファイル
	    FileWriter file = new FileWriter("gaussseidel.dat");
	    PrintWriter pw = new PrintWriter(new BufferedWriter(file));

	    // 行列次元の取得
	    int n = a.length;
	
	    if (n == 0){
		throw new IllegalArgumentException("配列を使用して下さい．");
	    }
	    else if (a[0].length != n || b.length != n || x.length != n){
		throw new IllegalArgumentException("配列サイズが一致しません．");
	    }
	    else{
		double err = 0.0D;
		double oldnorm, newnorm, sumnorm;
		double [] newx = new double[n];
		double [] errvec = new double[n];
		
		newnorm = sumnorm = 0;
		oldnorm = UtilityLA.norm(x, 2);
			do{
				// [演習] ガウス・ザイデル法メソッドを完成させてください
				for (i = 0; i < n; i++) {
					newx[i] = b[i];
					for (j = 0; j < n; j++) {
						if (j != i) newx[i] -= a[i][j] * newx[j];
					}
					newx[i] /= a[i][i];
				}
				newnorm = UtilityLA.norm(newx, 2);
				sumnorm = oldnorm + newnorm;
				err = UtilityLA.norm(UtilityLA.vecpm(newx, x, 'm'), 2);
				for (i = 0; i < n; i++) {
					x[i] = newx[i];
				}
				oldnorm = newnorm;

				// 反復回数の更新・確認と収束履歴出力
				k++;
				pw.printf("%4d  %17.15e%n",k,err);
				if (k == IRMAX){
				System.err.println("最大反復回数に到達しました．終了します．");
				pw.close();
				return;
				}

			} while (err >= epsa + epsr * sumnorm);

		System.out.println("反復回数 (ガウス・ザイデル法) = "+k);
		pw.close();
	    }
	}
	catch(Exception e){
	    System.err.println(e);	    
	}
    }

    //
    // SOR法メソッド
    //  【入力】
    //   a = 係数行列 (2次元配列)
    //   b = 右辺ベクトル
    //   w = 過大緩和パラメータ (0 < w < 2)
    //   eps = 収束判定定数
    //   x = 初期ベクトル
    //
    //  【出力】 (実行後)
    //   x = 解ベクトル
    //
    void SORMethod(double[][] a, double[] b, double w, double[] x){
	int i, j, k = 0;

	try{
	    // 収束履歴ファイル
	    FileWriter file = new FileWriter("sor.dat");
	    PrintWriter pw = new PrintWriter(new BufferedWriter(file));

	    // 行列次元の取得
	    int n = a.length;

	    if(n == 0){
		throw new IllegalArgumentException("配列を使用して下さい．");
	    }
	    else if(a[0].length != n || b.length != n || x.length != n){
		throw new IllegalArgumentException("配列サイズが一致しません．");
	    }
	    else if (w <= 0.0D || w >= 2.0D){
		throw new IllegalArgumentException("過大緩和パラメータが不正です．");
	    }
	    else{
		double err = 0.0D;
		double oldnorm, newnorm, sumnorm;
		double [] newx = new double[n];
		double [] errvec = new double[n];

		newnorm = sumnorm = 0;
		oldnorm = UtilityLA.norm(x, 2);

		do{
			// [演習] SOR法メソッドを完成させてください
			for (i = 0; i < n; i++) {
				newx[i] = b[i];
				for (j = 0; j < n; j++) {
					if (j != i) newx[i] -= a[i][j] * newx[j];
				}
				newx[i] /= a[i][i];
				newx[i] = x[i] + w * (newx[i] - x[i]);
			}
			newnorm = UtilityLA.norm(newx, 2);
			sumnorm = oldnorm + newnorm;
			err = UtilityLA.norm(UtilityLA.vecpm(newx, x, 'm'), 2);
			for (i = 0; i < n; i++) {
				x[i] = newx[i];
			}
			oldnorm = newnorm;

		    // 反復回数の更新・確認と収束履歴出力
		    k++;
		    pw.printf("%4d  %17.15e%n",k,err);
		    if(k == IRMAX){
			System.err.println("最大反復回数に到達しました．終了します．");
			pw.close();
			return;
		    }
		} while (err >= epsa + epsr * sumnorm);

		System.out.println("反復回数 (SOR法) = "+k);
		pw.close();
	    }
	}
	catch(Exception e){
	    System.err.println(e);
	}
    }
}
