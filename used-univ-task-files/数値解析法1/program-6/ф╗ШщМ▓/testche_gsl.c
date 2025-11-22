/*
数値計算ライブラリ GSL を用いたチェビシェフ近似実行プログラムです．

[UbuntuなどLinux, Windows（MSYS2+MinGW）の場合の場合]
gcc -o testche_gsl testche_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testche_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testche_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testche_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_errno.h>
#include <gsl/gsl_chebyshev.h>

/* 確認用関数 */
double testf(double x, void *p)
{
  (void)(p); /* avoid unused parameter warning */
  
  /* 例題2 (ルンゲの現象) */
  return(1.0/(1.0+25.0*x*x));
}

/*
 演習問題確認プログラム (チェビシェフ近似)
*/
int main(void)
{
  int i,k;
  double maxerr = 0.0, ipval, errval;
  FILE *fp;

  /* 分割数 */
  int n = 16;

  /* チェビシェフ近似用GSLオブジェクト */
  gsl_cheb_series *cs = gsl_cheb_alloc(n);

  gsl_function F;

  F.function = testf;
  F.params = 0;

  /* 区間 */
  /* 例題2 (ルンゲの現象) */
  double a = -1.0, b = 1.0;

  /* チェビシェフ近似オブジェクトの初期化 */
  gsl_cheb_init(cs, &F, a, b);

  double xp,width;

  /* 補間値の計算 */
  /* [a,b]を200等分割してファイルへ出力 */
  fp = fopen("exc-ip-gsl-cheby.dat","w");
  width = (b - a)/200;
  for(k = 0; k < 200; k++)
  {
    xp = a + k * width;
    /* GSL: チェビシェフ近似による補間値計算 */
    ipval = gsl_cheb_eval(cs,xp);
    errval = fabs(ipval - GSL_FN_EVAL(&F,xp));
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",xp,ipval,GSL_FN_EVAL(&F,xp),errval);
    if(maxerr < errval) maxerr = errval;
  }
  /* GSL: チェビシェフ近似による補間値計算 */
  ipval = gsl_cheb_eval(cs,b);
  errval = fabs(ipval - GSL_FN_EVAL(&F,b));
  fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",b,ipval,GSL_FN_EVAL(&F,b),errval);
  if(maxerr < errval) maxerr = errval;

  printf("Max absolute error = %16.15e\n",maxerr);
  
  fclose(fp);

  /* GSL: オブジェクトの解放 */
  gsl_cheb_free(cs);
  
  return(0);
}
