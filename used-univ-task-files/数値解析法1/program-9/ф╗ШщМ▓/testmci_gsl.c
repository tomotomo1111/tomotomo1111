/*
数値計算ライブラリ GSL を用いたモンテカルロ法による数値積分プログラムです

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testmci_gsl testmci_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testmci_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testmci_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testmci_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_monte.h>
#include <gsl/gsl_monte_plain.h>

/* 確認用関数 */
double testf(double *x, size_t dim, void *params)
{
  int i;
  double xval =0.0;
  /* (void)(dim); */ /* avoid unused parameter warnings */
  (void)(params);
  for(i = 0; i < dim; i++) xval += x[i];
  return(exp(-xval));
}

/*
 演習問題確認プログラム (GSLによるモンテカルロ法)
*/
int main(void)
{
  double mcval,err,mcerr;

  /* 空間次元 */
  int n = 5;
  
  /* 矩形領域区間 */
  double rmin[] = {0.0,0.0,0.0,0.0,0.0};
  double rmax[] = {1.0,1.0,1.0,1.0,1.0};

  /* 真の値 */
  double eval = pow(1.0-exp(-1.0),5);

  /* GSL: 乱数設定 */
  const gsl_rng_type *T;
  gsl_rng *r;

  gsl_rng_env_setup();

  T = gsl_rng_default;
  r = gsl_rng_alloc(T);

  /* GSL: 被積分関数の設定 */
  gsl_monte_function F = { &testf, n, 0 };

  /* GSL: 積分点数 */
  size_t calls = 500000;

  /* GSL: モンテカルロ法による数値積分計算 */
  gsl_monte_plain_state *s = gsl_monte_plain_alloc(n);
  gsl_monte_plain_integrate(&F, rmin, rmax, n, calls, r, s, &mcval, &err);
  gsl_monte_plain_free(s);

  printf("MCI (GSL)  = %17.15e\n",mcval);
  printf("Abs. error = %17.15e\n",fabs(mcval-eval));

  gsl_rng_free(r);
  
  return(0);
}
