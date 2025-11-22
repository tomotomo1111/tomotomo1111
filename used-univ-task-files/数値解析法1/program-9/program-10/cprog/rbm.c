/*
ロンバーグ積分法
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* 複合台形公式計算関数 */
double ncftrap(double a, double b, int n, double (* f)(double));

/*
  ロンバーグ積分法による積分計算
  [入力]
    k: 分割最大数に関する定数
    a, b: 区間
    f: 関数ポインタ（被積分関数）
  [返却値]
    数値積分値
*/
double rbm(int k, double a, double b, double (* f)(double)){
  int i, j, l, N = 2;
  double h, rval;

  // 複合台形公式の計算結果格納用配列
  double *rom;
  rom = (double *)calloc(k+1,sizeof(double));
  if(rom == NULL){
    printf("領域の確保に失敗\n");
    exit(1);
  }

  /* 複合台形公式による数値積分 */
  for (j = 0; j <= k; j++){
    rom[j] = ncftrap(a, b, N, f);
    N *= 2;
  }

  /* [演習]  ロンバーグ積分法関数を完成させてください */

  rval = rom[k];
  free(rom);

  return(rval);
}
