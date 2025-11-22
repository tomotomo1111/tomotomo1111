/*
DFTプログラム
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/*
  DFT計算プログラム
  [入力]
    n: サンプリング数
    f: 1次元配列 (n次元, サンプリング値ベクトル)
  [出力]
    c: 2次元配列 (n次元, DFT行列)
 */
void dft(int n, double *f, double **c)
{
  int j, k;

  /* 回転因子角度 */
  double theta = 2 * M_PI / n;

  /* [演習] DFT関数を完成させてください */

}

/*
  IDFT計算プログラム
  [入力]
    n: サンプリング数
    c: 2次元配列 (n次元, DFT行列)
  [出力]
    f: 2次元配列 (n次元, サンプリング値行列)
 */
void idft(int n, double **c, double **f){
  int j, k;
 
  /* 回転因子角度 */
  double theta = 2 * M_PI / n;

  /* [演習] IDFT関数を完成させてください */

}
