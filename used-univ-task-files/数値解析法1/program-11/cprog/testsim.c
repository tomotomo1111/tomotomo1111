/*
このプログラムを使用する場合は，まずsitm.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testsim testsim.c sim.c lacalc.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testsim で実行できます．
計算結果を確認してください．

[実行例]
Converged (simple iteration): num. of iterations = 80
Approximate solution = (4.999999999999993e-01, 2.499999999999996e-01)
*/
#include <stdio.h>
#include <stdlib.h>

int simethod(int n, double *x, void (* f)(int,double *,double *));

/* 確認用関数 (ベクトル値関数) */
void testf(int n, double *x, double *val)
{
  int i;

  val[0] = 0.5*x[0]*x[0]+x[0]+0.25*x[1]-11.0/16;
  val[1] = 0.25*x[0]+0.5*x[1]*x[1]+x[1]-13.0/32;

  return;
}

/*
 演習問題確認プログラム (逐次反復法)
*/
int main(void)
{
  int rval;
  void (* func)(int,double*,double*);

  /* 次元 */
  int n = 2;

  /* 初期ベクトル */
  double x[2] = {0.0,0.0};

  /* 関数ポインタ */
  func = testf;

  /* 逐次反復法による求解 */
  rval = simethod(n,x,func);
  if (rval == 1) exit(1);

  printf("Approximate solution = (%17.15e, %17.15e)\n",x[0],x[1]);
  
  return(0);
}
