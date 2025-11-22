/*
数値計算ライブラリ GSL を用いたガウス・ルジャンドル公式による数値積分プログラムです

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testgl_gsl testgl_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testgl_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testgl_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testgl_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_integration.h>

/* 確認用関数 */
double testf(double x, void *params)
{
  (void)(params);
  /* 例題 */
  return(1/(x*x));
}

/*
 演習問題確認プログラム (ガウス・ルジャンドル公式)
*/
int main(void)
{
  int n = 8; /* 分点数を指定可能 */
  double glval,glerr;

  /* 積分区間 */
  double a = 1.0;
  double b = 2.0;

  /* 真の値 */
  double eval = 0.5;

  /* GSL 関数ポインタ */
  gsl_function F;
  F.function = &testf;
  F.params = 0;

  /* 使用する公式 */
  gsl_integration_fixed_workspace *w;
  const gsl_integration_fixed_type *T = gsl_integration_fixed_legendre;
  w = gsl_integration_fixed_alloc(T,n,a,b,0.0,0.0);
  
  /* 数値積分計算 */
  gsl_integration_fixed(&F,&glval,w);
  glerr = fabs(glval-eval);
  printf("Numerical int. (GSL) = %17.15e\n",glval);
  printf("Absolute error (GSL) = %17.15e\n",glerr);

  gsl_integration_fixed_free (w);
  
  return(0);
}
