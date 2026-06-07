/* アンダーフロー・オーバーフローサンプルプログラム sampleuof.c
   
   コンパイル： gcc -o sampleuof sampleuof.c

   ./sampleuof + Enterキーで実行できます．

      注) 文字コードはUTF-8，改行コードはLFです．
*/
#include <stdio.h>
#include <float.h> 

int main(void){
  float x, y;

  /* FLT_MIN: 単精度の正の最小数，FLT_MAX: 単精度の最大値 */
  /* 倍精度の場合は，FLTをDBLにすればよい */
  x = 2.0 * FLT_MAX;
  y = FLT_MIN / 2;

  printf("2 * xmax = %f\n",x);
  printf("xmin / 2 = %f\n",y);

  return(0);
}
