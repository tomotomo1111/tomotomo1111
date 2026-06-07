/* 結合律確認プログラム

   コンパイル：
   gcc -o samplefpap samplefpap.c -lm

   ./samplefpap + Enterキーで実行できます．

   注) 文字コードはUTF-8，改行コードはLFです．
*/
#include <stdio.h>
#include <math.h>

int main(void){
  double a = pow(2,53);
  double b = 1.0;
  double c = 1.0;
  double d = a + b; d += c;
  double e = a; e += b + c;

  printf("(a + b) + c = %17.15e\n",d);
  printf("a + (b + c) = %17.15e\n",e);

  return(0);
}
