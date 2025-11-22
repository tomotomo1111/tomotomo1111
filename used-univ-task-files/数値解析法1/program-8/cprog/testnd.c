/*
このプログラムを使用する場合は，まずnd.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testnd testnd.c nd.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testnd で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double fwdf(double a, double h, double (* f)(double));
double bkdf(double a, double h, double (* f)(double));
double ctdf(double a, double h, double (* f)(double));

/* 確認用関数 */
double testf(double x)
{
  /* 例題 */
  return(exp(x));
}

/*
 演習問題確認プログラム (差分近似)
*/
int main(void)
{
  int k;
  double fval,bval,cval;
  double ferr,berr,cerr;
  double (* func)(double);
  FILE *fp;

  /* 差分を求める点 */
  double a = 1.0;

  /* 真の値 */
  double eval = exp(a);

  /* 分割幅（初期値）*/
  double h = 1.0;
  
  /* 最大分割数 */
  int n = 14;

  /* 関数ポインタ */
  func = testf;

  /* 差分近似値の計算 */
  fp = fopen("exc-dif-err.dat","w");
  printf("Absolute error\n");
  printf("h    forward     backward     central\n");
  for(k = 0; k <= n; k++)
  {
    fval = fwdf(a,h,func);
    ferr = fabs(fval-eval);

    bval = bkdf(a,h,func);
    berr = fabs(bval-eval);

    cval = ctdf(a,h,func);
    cerr = fabs(cval-eval);

    printf("%e  %e  %e  %e\n",h,ferr,berr,cerr);
    /* fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",log(1.0D/h),log(ferr),log(berr),log(cerr)); */
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",1.0D/h,ferr,berr,cerr);

    h /= 2.0;
  }
  
  fclose(fp);
  
  return(0);
}
