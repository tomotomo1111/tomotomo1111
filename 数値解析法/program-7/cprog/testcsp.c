/*
このプログラムを使用する場合は，まずspline.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testcsp testcsp.c spline.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testcsp で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int cscoef(int n, double *x, double *f, double *df, double *c);
double cspline(int n, double *x, double *f, double *c, double xp);
/* double lspline(int n, double *x, double *f, double xp); */

/* 確認用関数 */
double testf(double x)
{
  /* 例題 (ルンゲの現象) */
  return(1.0/(1.0+25.0*x*x));
}

/*
 演習問題確認プログラム (3次スプライン補間)
*/
int main(void)
{
  int i,k,rval;
  double maxerr = 0.0, ipval, errval;
  FILE *fp;

  /* 区間 */
  double a = -1.0, b = 1.0;

  /* 分点数-1 */
  int n = 16;

  double xp,h,width;
  double *x,*f,*df,*c;
  
  x = (double *)calloc(n+1,sizeof(double));
  f = (double *)calloc(n+1,sizeof(double));
  df = (double *)calloc(2,sizeof(double));
  c = (double *)calloc(n+1,sizeof(double));
  if(x == NULL || f == NULL || df == NULL || c == NULL)
  {
    printf("領域の確保に失敗\n");
    exit(1);
  }

  /* 分点と関数値の計算 */
  h = (b-a)/n;
  for(i = 0; i <= n ; i++)
  {
    x[i] = a + i*h;
    f[i] = testf(x[i]);
  }
  df[0] = 25.0/338.0; df[1] = -25.0/338.0;

  /* 係数の計算 */
  rval = cscoef(n,x,f,df,c);
  if (rval != 0) exit(1);

  /* 補間値の計算 */
  /* [a,b]を200等分割してファイルへ出力 */
  fp = fopen("exc-cs.dat","w");
  /* fp = fopen("exc-ls.dat","w"); */
  width = (b - a)/200;
  for(k = 0; k < 200; k++)
  {
    xp = a + k * width;
    ipval = cspline(n,x,f,c,xp);
    /* ipval = lspline(n,x,f,xp); */
    errval = fabs(ipval - testf(xp));
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",xp,ipval,testf(xp),errval);
    if(maxerr < errval) maxerr = errval;
  }
  ipval = cspline(n,x,f,c,b);
  /* ipval = lspline(n,x,f,b); */
  errval = fabs(ipval - testf(b));
  fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",b,ipval,testf(b),errval);
  if(maxerr < errval) maxerr = errval;

  printf("Max absolute error = %16.15e\n",maxerr);
  
  fclose(fp);

  free(x);
  free(f);
  free(df);
  free(c);
  
  return(0);
}
