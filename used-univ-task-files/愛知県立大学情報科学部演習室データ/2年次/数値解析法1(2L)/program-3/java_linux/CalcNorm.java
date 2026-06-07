//  コンパイル: javac CalcNorm.java
//  実行: java CalcNorm
//
// として下さい． なおノルムの計算には，UtilityLA クラスに
// 定義されているメソッド (static メソッド) を使用しています．
// UtilityLA.java を同一ディレクトリ内に置いておいて下さい．
//
// 【実行結果】
// ベクトルノルム：1ノルム        = 5.0500000000000000e+03
// ベクトルノルム：2ノルム        = 5.8167860541711530e+02
// ベクトルノルム：最大値ノルム   = 1.0000000000000000e+02
//
// 行列ノルム：1ノルム            = 5.0500000000000000e+03
// 行列ノルム：最大値ノルム       = 5.0500000000000000e+03
// 行列ノルム：フロベニウスノルム = 4.1235118527779205e+03
//
class CalcNorm {
	public static void main(String[] args) {
		int i,j;
		int m=100,n=100;

		// ベクトル
		double [] x = new double[n];
		for(i = 0; i < n; i++) x[i] = i+1;

		// ベクトルノルムの計算
		double vecnorm1 = UtilityLA.norm(x, 1); // 1ノルム
		double vecnorm2 = UtilityLA.norm(x, 2); // 2ノルム
		double vecnormmax = UtilityLA.norm(x, 0); // 最大値ノルム

		// 計算結果 (ベクトルノルム)
		System.out.printf("\nベクトルノルム：1ノルム        = %18.16e\n", vecnorm1);
		System.out.printf("ベクトルノルム：2ノルム        = %18.16e\n", vecnorm2);
		System.out.printf("ベクトルノルム：最大値ノルム   = %18.16e\n\n", vecnormmax);
		
		// 行列
		double [][] a = new double[m][n];

		for(i = 0; i < m; i++)
		{
			for(j = 0; j < n; j++)
			{
				a[i][j] = Math.min(i+1,j+1);
			}
		}

		// 行列ノルムの計算
		double matnorm1 = UtilityLA.norm(a, 1); // 1ノルム
		double matnormmax = UtilityLA.norm(a, 0); // 最大値ノルム
		double matnormf = UtilityLA.norm(a, 3); // フロベニウスノルム

		// 計算結果 (行列ノルム)
		System.out.printf("行列ノルム：1ノルム            = %18.16e\n", matnorm1);
		System.out.printf("行列ノルム：最大値ノルム       = %18.16e\n", matnormmax);
		System.out.printf("行列ノルム：フロベニウスノルム = %18.16e\n\n", matnormf);
	}
}
