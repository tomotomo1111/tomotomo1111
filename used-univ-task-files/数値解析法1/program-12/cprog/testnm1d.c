/*
このプログラムを使用する場合は，まずnm.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testnm1d testnm1d.c nm.c lacalc.c gausselm.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testnm1d で実行できます．
計算結果を確認してください．

 [実行例]
 Converged: num. of iterations = 8
 Sol. (Newton method) = 5.000000000000003e-01
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int newton1d(double *x, double (* f)(double), double (* df)(double));
  
/* 例題関数 (関数値, 6,17ページ) */
double testf(double x)
{
  return(pow(x,5)/24-13*pow(x,4)/48+17*pow(x,3)/24-23*x*x/24+2*x/3-1.0/6);
}

/* 例題関数 (導関数値, 6,17ページ) */
double testdf(double x)
{
  return(5*pow(x,4)/24-13*pow(x,3)/12+17*x*x/8-23*x/12+2.0/3);
}

/*
 演習問題確認プログラム (1次元ニュートン法)
*/
int main(void)
{
  int rval;
  double x;
  double (* func)(double);
  double (* dfunc)(double);

  /* 初期値 */
  double x0 = 0.0; /* 6ページ */
  /* double x0 = 9.9999999999999e-1; */ /* 17ページ */

  /* 関数ポインタ (6,17ページ例題) */
  func = testf;
  dfunc = testdf;

  x = x0;
  rval = newton1d(&x,func,dfunc);
  if (rval == 1) exit(1);
  printf("Sol. (Newton method) = %17.15e\n",x);
  
  return(0);
}
