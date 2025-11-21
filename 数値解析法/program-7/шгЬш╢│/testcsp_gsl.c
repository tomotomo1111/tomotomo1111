/*
数値計算ライブラリ GSL を用いた3次スプライン補間実行プログラムです．

[UbuntuなどLinux, Windows (MSYS2+MinGW) の場合]
gcc -o testcsp_gsl testcsp_gsl.c -lgsl -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testcsp_gsl -I(GSLインストールディレクトリ)/include -L(GSLインストールディレクトリ)/lib testcsp_gsl.c -lgsl -lm

コンパイルに成功した場合は，./testcsp_gsl で実行できます．
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
  /* 例題 */
  return(1.0/(1.0+25.0*x*x));
}

int main(void)
{
  int i,k;
  double maxerr = 0.0, ipval, errval;
  FILE *fp;

  gsl_interp_accel *acc = gsl_interp_accel_alloc();

  /* スプライン補間用のGSLオブジェクト */
  gsl_spline *cs;
  
  /* 区間 */
  double a = -1.0, b = 1.0;

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

  /* GSL: スプライン補間オブジェク補間方法の定義 */
  cs = gsl_spline_alloc(gsl_interp_cspline,n+1);
  
  /* GSL: オブジェクトの初期化 */
  gsl_spline_init(cs,x,f,n+1);

  /* 補間値の計算 */
  /* [a,b]を200等分割してファイルへ出力 */
  fp = fopen("exc-cs-gsl.dat","w");
  width = (b - a)/200;
  for(k = 0; k < 200; k++)
  {
    xp = a + k * width;
    /* GSL: 3次スプライン補間による補間値計算 */
    ipval = gsl_spline_eval(cs,xp,acc);
    errval = fabs(ipval - testf(xp));
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",xp,ipval,testf(xp),errval);
    if(maxerr < errval) maxerr = errval;
  }
  /* GSL: 3次スプライン補間による補間値計算 */
  ipval = gsl_spline_eval(cs,b,acc);
  errval = fabs(ipval - testf(b));
  fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",b,ipval,testf(b),errval);
  if(maxerr < errval) maxerr = errval;

  printf("Max absolute error = %16.15e\n",maxerr);
  
  fclose(fp);

  /* GSL: オブジェクトの解放 */
  gsl_spline_free(cs);
  gsl_interp_accel_free(acc);
  
  free(x);
  free(f);
  
  return(0);
}
