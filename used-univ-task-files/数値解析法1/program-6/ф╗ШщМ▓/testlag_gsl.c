/*
数値計算ライブラリ GSL を用いたラグランジュ補間実行プログラムです．

[UbuntuなどLinux, Windows（MSYS2+MinGW）の場合の場合]
gcc -o testlag_gsl testlag_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testlag_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testlag_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testlag_gsl で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_math.h>
#include <gsl/gsl_errno.h>
#include <gsl/gsl_spline.h>  /* GSL interpolation 関数使用に必要*/

/* 確認用関数 */
double testf(double x)
{
  /* 例題1 */
  return(cos(x));

  /* 例題2 (ルンゲの現象) */
  /* return(1.0/(1.0+25.0*x*x)); */
}

/*
 演習問題確認プログラム (ラグランジュ補間)
*/
int main(void)
{
  int i,k;
  double maxerr = 0.0, ipval, errval;
  FILE *fp;

  /* 補間用のGSLオブジェクト */
  gsl_interp *ws;

  /* 区間 */
  /* 例題1 */
  double a = -M_PI, b = M_PI;
  /* 例題2 (ルンゲの現象) */
  /* double a = -1.0, b = 1.0; */

  /* 分割数 */
  int n = 16;
  /* 分割幅 */
  double h = (b - a)/n;

  double xp,width;
  double *x,*f;
  
  x = (double *)calloc(n+1,sizeof(double));
  f = (double *)calloc(n+1,sizeof(double));
  if(x == NULL || f == NULL)
  {
    printf("領域の確保に失敗\n");
    exit(1);
  }

  /* 分点および関数値 */
  for(i = 0; i <= n ; i++)
  {
    x[i] = a + i * h;
    f[i] = testf(x[i]);
  }

  /* GSL: 補間方法の定義 */
  ws = gsl_interp_alloc(gsl_interp_polynomial,n+1);
  
  /* GSL: オブジェクトの初期化 (係数の計算など) */
  gsl_interp_init(ws,x,f,n+1);

  /* 補間値の計算 */
  /* [a,b]を200等分割してファイルへ出力 */
  fp = fopen("exc-ip-gsl.dat","w");
  width = (b - a)/200;
  for(k = 0; k < 200; k++)
  {
    xp = a + k * width;
    /* GSL: 補間多項式による補間値計算 */
    ipval = gsl_interp_eval(ws,x,f,xp,NULL);
    errval = fabs(ipval - testf(xp));
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",xp,ipval,testf(xp),errval);
    if(maxerr < errval) maxerr = errval;
  }
  /* GSL: 補間多項式による補間値計算 */
  ipval = gsl_interp_eval(ws,x,f,b,NULL);
  errval = fabs(ipval - testf(b));
  fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",b,ipval,testf(b),errval);
  if(maxerr < errval) maxerr = errval;

  printf("Max absolute error = %16.15e\n",maxerr);
  
  fclose(fp);

  /* GSL: オブジェクトの解放 */
  gsl_interp_free(ws);
  
  free(x);
  free(f);
  
  return(0);
}
