/* 桁落ちサンプルプログラム samplecd.c
   
   コンパイル： gcc -o samplecd samplecd.c -lm

   ./samplecd + Enterキーで実行できます．

      注) 文字コードはUTF-8，改行コードはLFです．
*/
#include <stdio.h>
#include <float.h>
#include<math.h> 

int getsol(double *coef, double *sol){
  int hantei = 0;
  double a,b,c,d,eps;
  a = coef[0]; b = coef[1]; c = coef[3];
  
  /* 判別式の計算 */
  d = b * b - 4 * a * c;

  if (fabs(d) <= DBL_EPSILON){
    /* 重解 */
    sol[0] = -b / (2 * a);
    sol[1] = sol[0];
  }
  else if (d < -DBL_EPSILON) {
    /* 共役複素数解：計算しない */
    hantei = 1;
    sol[0] = 0;
    sol[1] = 0;
  }
  else {
    /* 2つの異なる実数解 */
    hantei = 2;
    sol[0] = (-b + sqrt(d)) / (2 * a);
    sol[1] = (-b - sqrt(d)) / (2 * a);
  }

  return hantei;
}

int main(void){
  int rval;
  double coef[3];
  double sol[2];

  /* 係数の入力 */
  printf("2次方程式の係数を入力してください\n");
  printf("a = ");
  scanf("%lf",&coef[0]);
  printf("b = ");
  scanf("%lf",&coef[1]);
  printf("c = ");
  scanf("%lf",&coef[2]);

  /* printf("a = %17.15e\nb = %17.15e\nc = %17.15e\n",coef[0],coef[1],coef[2]); */

  /* 2次方程式の求解 */
  rval = getsol(coef,sol);

  if (rval == 0){
    printf("重解: 解 = %18.16e\n", sol[0]);
  }
  else if (rval == 1){
    printf("異なる2つの虚数解（計算しない）\n");
  }
  else {
    printf("異なる2つの実数解:\n解1 = %18.16e\n解2 = %18.16e\n",sol[0],sol[1]);
  }

  return(0);
}
