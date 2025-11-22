/*
このプログラムを使用する場合は，まずncf.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testncf testncf.c ncf.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testncf で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double ncftrap(double a, double b, int n, double (* f)(double));
double ncfsimp(double a, double b, int n, double (* f)(double));
double ncfmidp(double a, double b, int n, double (* f)(double));

/* 確認用関数 */
double testf(double x)
{
  /* 例題 */
  return(1/(x*x));
}

/*
 演習問題確認プログラム (ニュートン・コーツの公式)
*/
int main(void)
{
  int k,n;
  double tval,sval,mval;
  double terr,serr,merr;
  double (* func)(double);
  FILE *fp;

  /* 積分区間 */
  double a = 1.0;
  double b = 2.0;

  /* 真の値 */
  double eval = 0.5;

  /* 分割幅（初期値）*/
  double h = 1.0;
  
  /* 最大分割指数 2^n */
  int nmax = 6;

  /* 関数ポインタ */
  func = testf;

  /* 複合公式による数値積分値の計算 */
  fp = fopen("exc-ncf-err.dat","w");
  printf("Absolute error\n");
  printf("h    trapezoidal     simpson     midpoint\n");
  n = 1;
  for(k = 1; k <= nmax; k++)
  {
    n *= 2;
    tval = ncftrap(a,b,n,func);
    terr = fabs(tval-eval);

    sval = ncfsimp(a,b,n,func);
    serr = fabs(sval-eval);

    mval = ncfmidp(a,b,n,func);
    merr = fabs(mval-eval);

    printf("%3d  %e  %e  %e\n",n,terr,serr,merr);
    /* fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",1.0/((b-a)/n),log(terr),log(serr),log(merr)); */
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",1.0/((b-a)/n),terr,serr,merr);
  }
  
  fclose(fp);
  
  return(0);
}
