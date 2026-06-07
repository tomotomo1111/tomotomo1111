/* 計算機イプシロンサンプルプログラム samplemeps.c
   
   コンパイル： gcc -o samplemeps samplmeps.c

   ./samplemeps + Enterキーで実行できます．

      注) 文字コードはUTF-8，改行コードはLFです．
*/
#include <stdio.h>
#include<float.h> 

int main(void){
  double x = 1.0 + DBL_EPSILON;
  double y = 1.0 + DBL_EPSILON / 2;

  printf("1 + e         = %18.16e\n",x);
  printf("1 + e / 2     = %18.16e\n",y);

  return(0);
}
