/*
数値計算ライブラリ GSL を用いた1次元ニュートン法およびステファンセン反復による非線形方程式求解プログラムです

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testnm1d_gsl testnm1d_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testnm1d_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testnm1d_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testnm1d_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <float.h>
#include <gsl/gsl_errno.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_roots.h>

/* 例題関数 (関数値, 6,17ページ) */
double testf(double x, void *param)
{
  return(pow(x,5)/24-13*pow(x,4)/48+17*pow(x,3)/24-23*x*x/24+2*x/3-1.0/6);
}

/* 例題関数 (導関数値, 6,17ページ) */
double testdf(double x, void *param)
{
  return(5*pow(x,4)/24-13*pow(x,3)/12+17*x*x/8-23*x/12+2.0/3);
}

/* 例題関数 (関数値＋導関数値, 6,17ページ) */
void testfdf(double x, void *param, double *fval, double *dfval)
{
  *fval = pow(x,5)/24-13*pow(x,4)/48+17*pow(x,3)/24-23*x*x/24+2*x/3-1.0/6;
  *dfval = 5*pow(x,4)/24-13*pow(x,3)/12+17*x*x/8-23*x/12+2.0/3;

  return;
}

/*
 演習問題確認プログラム
*/
int main(void)
{
  int status;
  int iter = 0, itermax = 1000;
  double newx,x;
  /* 絶対許容誤差 */
  double epsa = DBL_MIN;
  /* double epsa = FLT_MIN; */

  /* 相対許容誤差 */
  double epsr = pow(2,3)*DBL_EPSILON;
  /* double epsr = FLT_EPSILON; */

  const gsl_root_fdfsolver_type *T;
  gsl_root_fdfsolver *s;
 
  gsl_function_fdf FDF;

  /* 初期値 */
  double x0 = 0.0; /* 6ページ */
  /* double x0 = 9.9999999999999e-1; */ /* 17ページ */

  /* 真の解 */
  double truex = 5.0e-1;

  /* GSL 関数ポインタ (6,17ページ例題) */
  FDF.f = &testf;
  FDF.df = &testdf;
  FDF.fdf = &testfdf;
  FDF.params = 0;

  T = gsl_root_fdfsolver_newton; /* ニュートン法 */
  /* T = gsl_root_fdfsolver_steffenson; */ /*ステファンセン反復 */
  s = gsl_root_fdfsolver_alloc(T);
  gsl_root_fdfsolver_set(s, &FDF, x0);

  printf("Using %s method\n", gsl_root_fdfsolver_name(s));
  printf("%5s %17s %16s\n", "iter", "solution", "abs error");

  x = x0;
  do
  {
    iter++;
    status = gsl_root_fdfsolver_iterate(s);
    newx = gsl_root_fdfsolver_root(s);
    status = gsl_root_test_delta(newx, x, epsa, epsr*(fabs(x)+fabs(newx))); /* 収束判定 */

    if(status == GSL_SUCCESS) printf ("Converged:\n");

    printf("%5d %17.15e %16.15e\n", iter, newx, fabs(newx - truex));
    x = newx;
  } while (status == GSL_CONTINUE && iter < itermax);
  
  gsl_root_fdfsolver_free(s);
  return(status);
}
