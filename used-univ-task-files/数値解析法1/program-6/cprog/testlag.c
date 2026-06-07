/*
このプログラムを使用する場合は，まずlagip.c内の関数lagcf,lagpを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testlag testlag.c lagip.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testlag で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void lagcf(int n, double *x, double *f, double *c);
double lagp(int n, double *x, double *c, double xp);

/* 確認用関数 */
double testf(double x)
{
  /* 例1 */
  return(cos(x));

  /* 例2 (ルンゲの現象) */
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

  /* 区間 */
  /* 例1 */
  double a = -M_PI, b = M_PI;
  /* 例2 (ルンゲの現象) */
  /* double a = -1.0, b = 1.0; */

  /* 補間多項式次数 */
  int n = 16;
  /* 分割幅 */
  double h = (b - a)/n;

  double xp,width;
  double *x,*f,*c;
  
  x = (double *)calloc(n+1,sizeof(double));
  f = (double *)calloc(n+1,sizeof(double));
  c = (double *)calloc(n+1,sizeof(double));
  if(x == NULL || f == NULL || c == NULL)
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

  /* 補間係数の計算 */
  lagcf(n,x,f,c);

  /* 補間値の計算 */
  /* [a,b]を200等分割してファイルへ出力 */
  fp = fopen("exc-ip.dat","w");
  width = (b - a)/200;
  for(k = 0; k < 200; k++)
  {
    xp = a + k * width;
    ipval = lagp(n,x,c,xp);
    errval = fabs(ipval - testf(xp));
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",xp,ipval,testf(xp),errval);
    if(maxerr < errval) maxerr = errval;
  }
  ipval = lagp(n,x,c,b);
  errval = fabs(ipval - testf(b));
  fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",b,ipval,testf(b),errval);
  if(maxerr < errval) maxerr = errval;

  printf("Max absolute error = %16.15e\n",maxerr);
  
  fclose(fp);

  free(x);
  free(f);
  free(c);
  
  return(0);
}
