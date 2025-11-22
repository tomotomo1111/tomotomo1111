//
//  コンパイル: javac UseLAIter.java
//  実行: java UseLAIter
//
// として下さい．ただし，LAIter.javaを完成させること．
// またベクトル関連の計算には，UtilityLA クラスに
// 定義されているメソッド (static メソッド) を使用しています．
// UtilityLA.java（完成品）を同一ディレクトリ内に置いておいて下さい．
//
// 【実行結果（行列1の場合）】
// 反復回数 (ヤコビ法) = 53
// 相対誤差 (ヤコビ法) = 1.9982994928702615e-16
// 
// 反復回数 (ガウス・ザイデル法) = 29
// 相対誤差 (ガウス・ザイデル法) = 2.2478037059277494e-16
// 
// 反復回数 (SOR法) = 36
// 相対誤差 (SOR法, w = 9.00e-01) = 2.2568492302247964e-16
// 
// 反復回数 (SOR法) = 27
// 相対誤差 (SOR法, w = 1.05e+00) = 2.0386732581609207e-16
// 
// 反復回数 (SOR法) = 80
// 相対誤差 (SOR法, w = 1.50e+00) = 1.2111523905001722e-16
//
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.IOException;
import java.util.Arrays;

class UseLAIter{
    public static void main(String[] args){
		// 次元
		int n = 100;

		// 係数行列
		double [][] a = new double[n][n];
		// 右辺ベクトル
		double [] b = new double[n];
		// 真の解ベクトル
		double [] exactx = new double[n];

		// ローカル変数
		int i, j, rval;
		double exactnorm, errnorm;

		// 真の解ベクトル
		Arrays.fill(exactx,1.1D);

		// 行列1
		double h = 1.0D / n;
		a[0][0] = 1.0D;
		a[n-1][n-1] = 1.0D;
		for(i = 1; i < n-1; i++){
	    	a[i][i-1] = 1.0D / h;
	    	a[i][i] = 4.0D / h;
	    	a[i][i+1] = 1.0D / h;
		}
		
		// 行列2
		/*for (i = 0 ; i < n ; i++){
	    	for (j = 0 ; j < n ; j++){
				a[i][j] = Math.min(i + 1, j + 1);
	    	}
		}*/

		// 右辺ベクトル
		b = UtilityLA.mulMatVec(a,exactx);
		
		// 真の解ベクトルのノルム計算
		exactnorm = UtilityLA.norm(exactx, 2);

		// 誤差ベクトル
		double [] errvec = new double[n];

		// 初期ベクトル
		double [] jbx = new double[n]; // ヤコビ法
		double [] gsx = new double[n]; // ガウス・ザイデル法

		// 定常反復法オブジェクトの生成
		LAIter li = new LAIter();

		// 解ベクトルの計算
		li.JacobiMethod(a, b, jbx);	 // ヤコビ法

		// 相対誤差計算 (ヤコビ法)
		errvec = UtilityLA.vecpm(jbx, exactx, 'm');
		errnorm = UtilityLA.norm(errvec, 2);
		System.out.printf("相対誤差 (ヤコビ法) = %18.16e\n\n", errnorm/exactnorm);

		// 解ベクトルの計算
		li.GaussSeidelMethod(a, b, gsx); // ガウス・ザイデル法

		// 相対誤差計算 (ガウス・ザイデル法)
		errvec = UtilityLA.vecpm(gsx, exactx, 'm');
		errnorm = UtilityLA.norm(errvec, 2);
		System.out.printf("相対誤差 (ガウス・ザイデル法) = %18.16e\n\n", errnorm/exactnorm);

		// 初期ベクトル
		double [] srx = new double[n]; // SOR法
	
		double w = 0.9D; // 過大緩和パラメータ1
		li.SORMethod(a, b, w, srx); // SOR法 (w = 0.9)

		// 相対誤差計算 (SOR法, w = 0.9)
		errvec = UtilityLA.vecpm(srx, exactx, 'm');
		errnorm = UtilityLA.norm(errvec, 2);
		System.out.printf("相対誤差 (SOR法, w = %3.2e) = %18.16e\n\n", w, errnorm/exactnorm);
		Path p0 = Paths.get("sor.dat");
		Path p1 = Paths.get("sor1.dat");
		try{
  			Files.move(p0, p1, REPLACE_EXISTING);
		}catch(IOException e){
  			System.out.println(e);
		}

		Arrays.fill(srx, 0.0D);
    	w = 1.05D; // 過大緩和パラメータ2
		li.SORMethod(a, b, w, srx); // SOR法 (w = 1.05)

		// 相対誤差計算 (SOR法, w = 1.05)
		errvec = UtilityLA.vecpm(srx, exactx, 'm');
		errnorm = UtilityLA.norm(errvec, 2);
		System.out.printf("相対誤差 (SOR法, w = %3.2e) = %18.16e\n\n", w, errnorm/exactnorm);
		Path p2 = Paths.get("sor2.dat");
		try{
  			Files.move(p0, p2, REPLACE_EXISTING);
		}catch(IOException e){
  			System.out.println(e);
		}

		Arrays.fill(srx, 0.0D);
    	w = 1.5D; // 過大緩和パラメータ3
		li.SORMethod(a, b, w, srx); // SOR法 (w = 1.5)

		// 相対誤差計算 (SOR法, w = 1.5)
		errvec = UtilityLA.vecpm(srx, exactx, 'm');
		errnorm = UtilityLA.norm(errvec, 2);
		System.out.printf("相対誤差 (SOR法, w = %3.2e) = %18.16e\n", w, errnorm/exactnorm);
		Path p3 = Paths.get("sor3.dat");
		try{
  			Files.move(p0, p3, REPLACE_EXISTING);
		}catch(IOException e){
  			System.out.println(e);
		}
    }
}
