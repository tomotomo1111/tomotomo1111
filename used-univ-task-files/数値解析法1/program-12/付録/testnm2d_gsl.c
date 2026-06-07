/*
数値計算ライブラリ GSL を用いた多次元ニュートン法による非線形方程式求解プログラムです

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testnm2d_gsl testnm2d_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testnm2d_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testnm2d_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testnm2d_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_vector.h>
#include <gsl/gsl_multiroots.h>

/* 例題関数 (ベクトル値関数,13ページ) */
int testf(const gsl_vector *x, void *params, gsl_vector *val)
{
  const double x0 = gsl_vector_get(x,0);
  const double x1 = gsl_vector_get(x,1);

  const double v0 = 0.5*x0*x0+x0+0.25*x1-11.0/16;
  const double v1 = 0.25*x0+0.5*x1*x1+x1-13.0/32;

  gsl_vector_set(val,0,v0);
  gsl_vector_set(val,1,v1);

  return(GSL_SUCCESS);
}

/* 例題関数 (ヤコビ行列,13ページ) */
int testdf(const gsl_vector *x, void *params, gsl_matrix *jm)
{
  const double x0 = gsl_vector_get(x,0);
  const double x1 = gsl_vector_get(x,1);
  
  const double j00 = x0 + 1.0;
  const double j01 = 0.25;
  const double j10 = 0.25;
  const double j11 = x1 + 1.0;

  gsl_matrix_set(jm,0,0,j00);
  gsl_matrix_set(jm,0,1,j01);
  gsl_matrix_set(jm,1,0,j10);
  gsl_matrix_set(jm,1,1,j11);

  return(GSL_SUCCESS);
}

/* 例題関数 (ベクトル値＋ヤコビ行列,13ページ) */
int testfdf(const gsl_vector *x, void *params, gsl_vector *val, gsl_matrix *jm)
{
  testf(x,params,val);
  testdf(x,params,jm);

  return(GSL_SUCCESS);
}

/*
 演習問題確認プログラム
*/
int main(void)
{
  const gsl_multiroot_fdfsolver_type *T;
  gsl_multiroot_fdfsolver *s;

  int status;
  int iter, itermax = 1000;

  /* 許容誤差 */
  double epsr = pow(2,3)*DBL_EPSILON;
   
  /* 次元 */
  int n = 2;

  gsl_multiroot_function_fdf func = {&testf,&testdf,&testfdf,n,0};

  /* 初期ベクトル */
  double x0[2] = {0.0,0.0};

  gsl_vector *x = gsl_vector_alloc(n);
  gsl_vector_set(x,0,x0[0]);
  gsl_vector_set(x,1,x0[1]);

  T = gsl_multiroot_fdfsolver_gnewton;
  s = gsl_multiroot_fdfsolver_alloc(T, n);
  gsl_multiroot_fdfsolver_set(s,&func,x);

  printf("%5s  %17s %17s\n","iter","x0","x1");
  printf("%5d  %17.15e  %17.15e\n",iter,gsl_vector_get(s->x,0),gsl_vector_get(s->x, 1));

  do
  {
      iter++;
      status = gsl_multiroot_fdfsolver_iterate(s);
      if(status) break;
      status = gsl_multiroot_test_residual(s->f,epsr);
      if(status == GSL_SUCCESS) printf ("Converged!!\n");
      printf("%5d  %17.15e  %17.15e\n",iter,gsl_vector_get(s->x,0),gsl_vector_get(s->x, 1));
    }
  while (status == GSL_CONTINUE && iter < itermax);

  gsl_multiroot_fdfsolver_free(s);
  gsl_vector_free(x);
  
  return(0);
}
