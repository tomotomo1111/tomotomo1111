//
// 【二重指数型積分公式クラス  DE】
//
public class DE {
	//
	// 有限区間に対するDE積分公式による積分値計算
	//  【入力】
	//   a = 積分区間左端点
	//   b = 積分区間右端点
	//   h = 分割幅
	//   M, N = 項数
	//   f = 被積分関数値格納配列
	//
	//  【返却値】
	//   DE積分公式による積分値
	//
	double DEBD(double a, double b, double h, int M, int N, double[] f)
	{
		int l;
		double r = (b - a) / 2;
		double rval = 0.0D;
		double tanhval = 0.0D;
		
		if (a >= b)
		{
			throw new IllegalArgumentException("区間端点の値に問題があります．");
		}
		else if (h <= 0)
		{
			throw new IllegalArgumentException("分割幅の値に問題があります");
		}
		else
		{
			// [演習] 有限区間の積分に対する二重指数関数型積分公式メソッドを完成させてください
		}
		
		return(rval);
	}
		
	//
	// 有限区間に対する変換関数
	//  【入力】
	//   a = 積分区間左端点
	//   b = 積分区間右端点
	//   t = 変数
	//
	//  【返却値】
	//   変換関数値
	//
	// * 第10回講義スライド14ページ掲載の変換関数です．
	//
	double TFBound(double a, double b, double t)
	{
		double r = (b - a) / 2;
		double c = (a + b) / 2;
		
		return(r * Math.tanh(Math.PI * Math.sinh(t) / 2) + c);
	}
	
	//
	// 半無限区間 [0,infinity) に対するDE積分公式による積分値計算
	//  【入力】
	//   h = 分割幅
	//   M, N = 項数
	//   f = 被積分関数値格納配列
	//
	//  【返却値】
	//   DE積分公式による積分値
	//
	double DESI(double h, int M, int N, double[] f)
	{
		int l;
		double t, rval = 0.0D;
		
		if (h <= 0)
		{
			throw new IllegalArgumentException("分割幅の値に問題があります");
		}
		else
		{
			for (l = -M ; l <= N ; l++){
				t = l*h;
				rval += f[l + M] * Math.PI * Math.cosh(t) * Math.exp(Math.PI * Math.sinh(t) / 2) / 2;
			}
			rval *= h;
		}
		
		return(rval);
	}
	
	//
	// 半無限区間 (0, Infinity) に対する変換関数
	//  【入力】
	//   t = 変数
	//
	//  【返却値】
	//   変換関数値
	//
	// * 第10回講義スライド15ページ掲載の変換関数です．
	//
	double TFSemiInf(double t)
	{
		return(Math.exp(Math.PI * Math.sinh(t) / 2));
	}
	
	//
	// 無限区間に対するDE積分公式による積分値計算
	//  【入力】
	//   h = 分割幅
	//   M, N = 項数
	//   f = 被積分関数値格納配列
	//
	//  【返却値】
	//   DE積分公式による積分値
	double DEIF(double h, int M, int N, double[] f)
	{
		int l;
		double rval = 0.0D;
		
		if (h <= 0)
		{
			throw new IllegalArgumentException("分割幅の値に問題があります");
		}
		else
		{
			// [演習] 無限積分に対する二重指数関数型積分公式メソッドを完成させてください
		}
		
		return(rval);
	}
	
	// 無限区間 (-Infinity, Infinity) に対する変換関数
	//  【入力】
	//   t = 変数
	//
	//  【返却値】
	//   変換関数値
	//
	// * 第10回講義スライド16ページ掲載の変換関数です．
	//
	double TFInf(double t)
	{
		return(Math.sinh(Math.PI * Math.sinh(t) / 2));
	}
}
