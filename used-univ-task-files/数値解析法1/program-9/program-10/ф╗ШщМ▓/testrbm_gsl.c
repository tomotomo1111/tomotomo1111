/*
数値計算ライブラリ GSL を用いたロンバーグ積分法式による数値積分プログラム

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testrbm_gsl testrbm_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testrbm_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testrbm_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testrbm_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_integration.h>
#include <gsl/gsl_errno.h>

/* 確認用関数 */
double testf(double x, void *params)
{
  (void)(params);
  /* 例題 */
  return(1/(x*x));
}

/*
 演習問題確認プログラム (ロンバーグ積分法)
*/
int main(void)
{
  int k = 10;
  double rbmval, rbmerr;

  /*  GSL用変数 */
  int rbmrst;
  size_t neval;
  double epsrel = 1.0e-8, epsabs = 1.0e-6;

  /* 積分区間 */
  double a = 1.0;
  double b = 2.0;

  /* 真の値 */
  double eval = 0.5;

  /* GSL 関数ポインタ */
  gsl_function F;
  F.function = &testf;
  F.params = 0;

  /* ロンバーグ積分法設定 */
  gsl_integration_romberg_workspace *w;
  w = gsl_integration_romberg_alloc(k);
  
  /* 数値積分計算 */
  rbmrst = gsl_integration_romberg(&F, a, b, epsabs, epsrel, &rbmval, &neval, w);
  if (rbmrst == GSL_SUCCESS){
    rbmerr = fabs(rbmval-eval);
    printf("Numerical int. (GSL) = %17.15e\n",rbmval);
    printf("Absolute error (GSL) = %17.15e\n",rbmerr);
  }
  else{
    printf("Fail: Romberg method\n");
  }

  gsl_integration_romberg_free (w);
  
  return(0);
}
