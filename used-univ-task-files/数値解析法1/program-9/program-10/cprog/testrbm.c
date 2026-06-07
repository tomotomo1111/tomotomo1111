/*
このプログラムを使用する場合は，まずrbm.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testrbm testrbm.c ncf.c rbm.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testrbm で実行できます．
計算結果を確認してください．

[計算結果]
Romberg method:
Approx. value = 5.000000183315112e-01 (abs. error = 1.833151119789989e-08)
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double rbm(int k, double a, double b, double (* f)(double));

/* 確認用関数: 有限区間 */
double testf(double x){
  return(1.0/(x*x));
}

/*
 演習問題確認プログラム (ロンバーグ積分法)
*/
int main(void){
  double rbmval,rbmerr;
  double (* f)(double);

  /* 積分区間 */
  double a = 1.0, b = 2.0, h;

  /* 真の値 */
  double eval = 1.0/2;

  /* 関数ポインタ */
  f = testf;

  /* 分割数用変数 */
  int k = 3;

  /* ロンバーグ積分法による数値積分値計算 */
  rbmval = rbm(k, a, b, f);
  rbmerr = fabs(rbmval - eval);
  printf("Romberg method:\nApprox. value = %17.15e (abs. error = %17.15e)\n", rbmval, rbmerr);

  return(0);
}
