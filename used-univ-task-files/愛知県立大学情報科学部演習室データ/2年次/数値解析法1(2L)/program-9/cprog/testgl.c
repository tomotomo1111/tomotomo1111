/*
このプログラムを使用する場合は，まずgl.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testgl testgl.c gl.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testgl で実行できます．
計算結果を確認してください．

[計算結果]
Numerical int. = 4.999999999929175e-01
Absolute error = 7.082501252142492e-12
Relative error = 1.416500250428498e-11
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double glint(double a, double b, double (* f)(double));

/* 確認用関数 */
double testf(double x)
{
  /* 例題 */
  return(1/(x*x));
}

/*
 演習問題確認プログラム (8分点ガウス・ルジャンドル公式)
*/
int main(void)
{
  int k,n;
  double glval,glerr;
  double (* func)(double);

  /* 積分区間 */
  double a = 1.0;
  double b = 2.0;

  /* 真の値 */
  double eval = 0.5;

  /* 関数ポインタ */
  func = testf;

  /* 数値積分計算 */
  glval = glint(a,b,func);
  glerr = fabs(glval-eval);
  printf("Numerical int. = %17.15e\n",glval);
  printf("Absolute error = %17.15e\n",glerr);
  printf("Relative error = %17.15e\n",glerr/fabs(eval));
  
  return(0);
}
