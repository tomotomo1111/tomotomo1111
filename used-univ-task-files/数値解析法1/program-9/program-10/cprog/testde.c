/*
このプログラムを使用する場合は，まずde.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testde testde.c de.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testde で実行できます．
計算結果を確認してください．

[計算結果]
DE formula for integration on finite interval
  N   Approx. value  Abs. error
   4  1.1109878e+00  4.4432116e-01
   8  6.8703258e-01  2.0365911e-02
  16  6.6667475e-01  8.0795215e-06
  32  6.6666667e-01  1.7230661e-13

DE formula for integration on infinite interval
  N   Approx. value  Abs. error
   4  3.3002445e+00  1.5865182e-01
   8  3.1435080e+00  1.9153253e-03
  16  3.1415927e+00  1.9715912e-08
  32  3.1415927e+00  4.4408921e-16
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double debd(double a, double b, double h, int M, int N, double (* f)(double));
double deif(double h, int M, int N, double (* f)(double));

/* 確認用関数: 有限区間 */
double testfb(double x){
  return(sqrt(x));
  /* return(1.0/sqrt(1.0 - x*x)); */
}

/* 確認用関数: 無限積分 */
double testfi(double x){
  return(1.0/(1.0 + x*x));
}

/*
 演習問題確認プログラム (二重指数関数型積分公式)
*/
int main(void){
  double debval,deberr,deival,deierr;
  double (* funcb)(double), (* funci)(double);
  FILE *fpb, *fpi;

  /* 積分区間: 有限区間の例 */
  double a = 0.0, b = 1.0, h;
  /* double a = -1.0, b = 1.0, h; */

  /* 分割幅計算用変数 */
  double xsup = 4.0;

  /* 真の値: 有限区間 */
  double ebval = 2.0/3;
  /* double ebval = 2.0/3; */
  double eival = M_PI;

  /* 関数ポインタ */
  funcb = testfb;
  funci = testfi;

  /* 分割数用変数 */
  int N = 1, k, kmax = 4;

  /* 有限区間の積分に対するDE公式計算 */
  fpb = fopen("exc-de-bound.dat","w");
  fprintf(fpb,"%4s  %-20s  %-20s\n","N","Approx. value","Abs error");
  printf("DE formula for integration on finite interval\n");
  printf("  N   Approx. value  Abs. error\n");
  for (k = 1; k <= kmax; k++){
    N *= 2;
    h = xsup / N;
    debval = debd(a, b, h, N, N, funcb);
    deberr = fabs(debval - ebval);
    fprintf(fpb,"%4d  %17.15e  %17.15e\n", 2*N, debval, deberr);
    printf("%4d  %9.7e  %9.7e\n", 2*N, debval, deberr);
  }
  fclose(fpb);

  /* 無限積分に対するDE公式計算 */
  fpi = fopen("exc-de-inf.dat","w");
  fprintf(fpb,"%4s  %-20s  %-20s\n","N","Approx. value","Abs error");
  printf("\nDE formula for integration on infinite interval\n");
  printf("  N   Approx. value  Abs. error\n");
  N = 1;
  for (k = 1; k <= kmax; k++){
    N *= 2;
    h = xsup / N;
    deival = deif(h, N, N, funci);
    deierr = fabs(deival - eival);
    fprintf(fpi,"%4d  %17.15e  %17.15e\n", 2*N, deival, deierr);
    printf("%4d  %9.7e  %9.7e\n", 2*N, deival, deierr);
  }
  fclose(fpi);

  return(0);
}
