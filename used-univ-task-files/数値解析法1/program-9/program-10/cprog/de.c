/*
二重指数関数型積分公式
*/
#include <stdio.h>
#include <math.h>

/*
 有限区間に対する変換関数
 [入力]
  a,b: 積分区間
  t: 変数
 [返却値]
  変換関数値
*/
double tfbound(double a, double b, double t){
  double r = (b - a) / 2;
  double c = (a + b) / 2;
  return(r * tanh(M_PI * sinh(t) / 2) + c);
}

/*
  有限区間に対するDE積分公式による積分計算
  [入力]
    a,b: 積分区間
    h: 分割幅
    M, N: 項数
    f: 関数ポインタ
  [返却値]
    数値積分値
*/
double debd(double a, double b, double h, int M, int N, double (* f)(double))
{
  /* [演習]  有限区間に対する二重指数関数型積分公式関数を完成させてください */
}

/*
 半無限区間(0, Infinity)に対する変換関数
 [入力]
  t = 変数t
 [返却値]
  変換関数値
*/
double tfsemiinf(double t){
  return(exp(M_PI * sinh(t) / 2));
}

/*
 半無限区間(0,Infinity)に対するDE公式による積分値計算関数
 [入力]
  h: 分割幅
  M, N: 項数
  f: 関数ポインタ
 [出力]
  数値積分値
*/
double desi(double h, int M, int N, double (* f)(double)){
  int l;
  double t, intval = 0.0;

  for (l = -M; l <= N; l++){
    t = l*h;
    intval += (* f)(tfsemiinf(t)) * M_PI * cosh(t) * exp(M_PI * sinh(t) / 2) / 2;
  }
  intval *= h;
  
  return(intval);
}

/*
 無限区間に対する変換関数
 [入力]
  t: 変数t
 [返却値]
  変換関数値
*/
double tfinf(double t){
  return(sinh(M_PI * sinh(t) / 2));
}

/*
 無限積分に対するDE公式による積分値計算
 [入力]
  h: 分割幅
  M, N: 項数
  f: 関数ポインタ
 [出力]
  数値積分値  
*/
double deif(double h, int M, int N, double (* f)(double)){
  /* [演習]  無限積分に対する二重指数関数型積分公式関数を完成させてください */
}
