/*
このプログラムを使用する場合は，まずstef.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o teststef teststef.c stef.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testatk で実行できます．
計算結果を確認してください．

[実行例]
Converged (Steffensen): num. of iterations = 7
Approximate solution = 4.999999999999987e-01
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int stefmethod(double *x, double (* f)(double));
  
/* 確認用関数 */
double testf(double x){
  return(pow(x,5)/24-13*pow(x,4)/48+17*pow(x,3)/24-23*x*x/24+2*x/3-1.0/6);
}

/*
 演習問題確認プログラム (ステファンセン反復)
*/
int main(void)
{
  int rval;
  double x;
  double (* func)(double);

  /* 初期値 */
  double x0 = 0.0;

  /* 関数ポインタ */
  func = testf;

  x = x0;
  rval = stefmethod(&x,func);
  if (rval == 1) exit(1);
  printf("Approximate solution = %17.15e\n",x);
  
  return(0);
}
