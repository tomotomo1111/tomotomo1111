/* gausselm.cが必要です．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testlsq testlsq.c gausselm.c lacalc.c lsq.c -lm
注) lacalc.c内の関数を使用しなかった場合は，lacalc.cを指定する必要はありません．
注) 演習室WSのUbuntuでも同じ

./testlsq で実行できます．計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int lsqcoef(int n, int m, double *x, double *f, double *c);
double lsqcalc(int m, double *c, double xp);

/*
 例題確認プログラム (最小二乗近似)
*/
int main(void)
{
  int i,k,rval;
  double lsqval,xp,width;
  FILE *fp,*dfp;

  /* データ数-1 */
  int n = 4;

  double x[] = {1.0,2.0,3.0,4.0,5.0};
  double f[] = {0.0,0.6,1.77,1.92,3.31};

  /* データの出力 */
  dfp = fopen("exc-xf.dat","w");
  for(i = 0; i <= n; i++) fprintf(dfp,"%17.15e  %17.15e\n",x[i],f[i]);
  fclose(dfp);

  /* 近似多項式の次元 */
  int m = 3;

  double *c;
  c = (double *)calloc(m+1,sizeof(double));
  if(c == NULL)
  {
    printf("領域の確保に失敗\n");
    exit(1);
  }

  /* 係数の計算 */
  rval = lsqcoef(n,m,x,f,c);
  if (rval != 0) exit(1);
  for(i = 0; i <=m ; i++) printf("c[%d] = %e\n",i,c[i]);

  /* 近似値の計算 */
  /* [a,b]を200等分割してファイルへ出力 */
  double a = 0.5, b = 5.5;
  fp = fopen("exc-lsq.dat","w");
  xp = a;
  width = (b - a)/200;
  for(k = 0; k <= 200; k++)
  {
    lsqval = lsqcalc(m,c,xp);
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",xp,lsqval);
    xp += width;
  }
  
  fclose(fp);

  free(c);
  
  return(0);
}
