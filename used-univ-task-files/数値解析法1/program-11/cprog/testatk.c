/*
このプログラムを使用する場合は，まずatk.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testatk testatk.c atk.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testatk で実行できます．
計算結果を確認してください．

[実行例]
Converged (simple iteration): num. of iterations = 243
Approximate solution = 4.999999999999868e-01

Converged (Aitken): num. of iterations = 117
Approximate solution = 4.999999999999846e-01
*/
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>

int atkmethod(double *x, double (* f)(double), bool bacc);
  
/* 確認用関数 */
double testf(double x)
{
  return(pow(x,5)/24-13*pow(x,4)/48+17*pow(x,3)/24-23*x*x/24+2*x/3-1.0/6);
}

/*
 演習問題確認プログラム (逐次反復法+エイトケン加速)
*/
int main(void)
{
  int rval;
  bool bacc; /* bacc = true -> 逐次反復法+エイトケン加速, false -> 逐次近似法のみ */
  double x;
  double (* func)(double);

  /* 初期値 */
  double x0 = 0.0;

  /* 関数ポインタ */
  func = testf;

  /* 逐次反復法のみの求解 */
  bacc = false;
  x = x0;
  rval = atkmethod(&x,func,bacc);
  if (rval == 1) exit(1);
  printf("Approximate solution = %17.15e\n\n",x);

  /* 逐次反復法+エイトケン加速の求解 */
  bacc = true;
  x = x0;
  rval = atkmethod(&x,func,bacc);
  if (rval == 1) exit(1);
  printf("Approximate solution = %17.15e\n",x);
  
  return(0);
}
