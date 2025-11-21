/* 情報落ちサンプルプログラム samplelg.c
   
   コンパイル： gcc -o samplelg samplelg.c -lm

   ./samplelg + Enterキーで実行できます．

      注) 文字コードはUTF-8，改行コードはLFです．
*/
#include <stdio.h>
#include<math.h> 

float calcsum(long int n){
  
  float sn = 0.0;

  for(long int k = 1; k <= n; k++) {
    sn += 1.0 / (k * k);
  }
  return(sn);
}

int main(void){
  long int n = 1;
  int Nnum = 16;
  float s, sn, err;

  /* 真の値 */
  s = (float)(M_PI * M_PI / 6);

  /* 部分和(n = 2, 2^2, ... , 2^10) による近似値の計算 */
  for (int i = 1; i <= Nnum; i++) {
    n *= 2;
    sn = calcsum(n);
    err = fabs(sn - s);
    printf("項数 %5ld: 近似値 = %9.7e (絶対誤差 = %9.7e)\n",n,sn,err);
  }

  return(0);
}
