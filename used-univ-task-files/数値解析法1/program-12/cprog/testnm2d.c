/*
このプログラムを使用する場合は，まずnm.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testnm2d testnm2d.c nm.c lacalc.c gausselm.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testnm2d で実行できます．
計算結果を確認してください．

[実行例]
Converged: num. of iterations = 6
Sol = (5.000000000000000e-01, 2.500000000000000e-01)
*/
#include <stdio.h>
#include <stdlib.h>

int newtonmd(int n, double *x, void (* f)(int,double *,double *), void (* df)(int,double *,double **));

/* 例題関数 (ベクトル値関数,13ページ) */
void testf(int n, double *x, double *val)
{
  val[0] = 0.5*x[0]*x[0]+x[0]+0.25*x[1]-11.0/16;
  val[1] = 0.25*x[0]+0.5*x[1]*x[1]+x[1]-13.0/32;

  return;
}

/* 例題関数 (ヤコビ行列,13ページ) */
void testfj(int n, double *x, double **jm)
{
  jm[0][0] = x[0] + 1.0;
  jm[0][1] = 0.25;
  jm[1][0] = 0.25;
  jm[1][1] = x[1] + 1.0;
  
  return;
}

/*
 演習問題確認プログラム (逐次反復法)
*/
int main(void)
{
  int rval;
  void (* func)(int,double*,double*);
  void (* jfunc)(int,double*,double**);

  /* 次元 */
  int n = 2;

  /* 初期ベクトル */
  double x[2] = {0.0,0.0};

  /* 関数ポインタ */
  func = testf;
  jfunc = testfj;

  /* 逐次反復法による求解 */
  rval = newtonmd(n,x,func,jfunc);
  if (rval == 1) exit(1);

  printf("Sol = (%17.15e, %17.15e)\n",x[0],x[1]);
  
  return(0);
}
