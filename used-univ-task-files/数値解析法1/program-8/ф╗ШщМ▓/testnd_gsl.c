/*
数値計算ライブラリ GSL を用いた差分近似プログラムです

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testnd_gsl testnd_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testnd_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testnd_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testnd_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_deriv.h>

double fwdf(double a, double h, double (* f)(double));
double bkdf(double a, double h, double (* f)(double));
double ctdf(double a, double h, double (* f)(double));

/* 確認用関数 */
double testf(double x, void *params)
{
  (void)(params);
  /* 例題 */
  return(exp(x));
}

/*
 演習問題確認プログラム (差分近似)
*/
int main(void)
{
  int k;
  double fval,bval,cval;
  double ferr,berr,cerr;
  double absferr,absberr,abscerr; /* 推定絶対誤差 */
  gsl_function F;

  /* 差分を求める点 */
  double a = 1.0;

  /* 真の値 */
  double eval = exp(a);

  /* 分割幅 (初期値) */
  double h = 1.0;
  
  /* GSL関数 */
  F.function = &testf;
  F.params = 0;

  /* 差分近似値の計算 */
  gsl_deriv_forward(&F,a,h,&fval,&absferr);
  ferr = fabs(fval-eval);

  gsl_deriv_backward(&F,a,h,&bval,&absberr);
  berr = fabs(bval-eval);

  gsl_deriv_central(&F,a,h,&cval,&abscerr);
  cerr = fabs(cval-eval);

  printf("Absolute error\n");
  printf("%e  %e  %e  %e\n",h,ferr,berr,cerr);
  
  return(0);
}
