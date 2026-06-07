/*
LU分解による求解プログラム
 */
#include <stdio.h>
#include <stdlib.h>
#include <float.h>
#include <math.h>

/* 行列のLU分解関数
  [入力]
    n: 行列の次元
    a: 2次元配列（行列）※n次正方行列である必要あり
    ipiv: ピボット選択結果格納用配列 ※スライドではシグマとしています
  [出力]
    a: 2次元配列（LU分解）
    ipiv: ピボット選択結果格納配列
  [返却値]
    実行結果：0 -> 正常終了（LU分解成功）, 1 -> 異常終了（行列が非正則）
*/
int ludecomp(int n, double **a, int *ipiv)
{
  int i,j,k,ip,ipivtemp;
  double amax,atemp;

  /* 置換結果ベクトルの初期化 */
  for (k = 0 ; k < n ; k++) ipiv[k] = k;

  /* [演習] LU分解プログラムを完成させてください． */
  
  return(0);
}

/* LU分解済行列による求解関数
  [入力]
    n: 行列・ベクトルの次元
    a: 2次元配列（LU分解済み行列）
    ipiv: 置換結果格納配列
    b: 1次元配列（右辺ベクトル）
  [出力]
    b: 1次元配列（近似解ベクトル）
  [返却値]
    実行結果：0 -> 正常終了（求解成功）, 1 -> 異常終了
  注) 右辺ベクトルをとっておきたい場合は，修正が必要
*/
int lusolve(int n, double **a, int *ipiv, double *b)
{
  int j,k;
  double *y;

  y = (double *)calloc(n,sizeof(double));
  if(y == NULL)
  {
    printf("領域の確保に失敗（一時格納ベクトル）\n");
    return(1);
  }
  
  /* 前進代入 (L y = P b を解く) */
  y[0] = b[ipiv[0]];
  for (k = 1 ; k < n ; k++)
  {
    y[k] = b[ipiv[k]];
    for (j = 0 ; j < k ; j++)
    {
      y[k] -= a[k][j] * y[j];
    }
  }

  /* 後退代入 (U x = y を解く) */
  b[n - 1] = y[n - 1] / a[n-1][n-1];
  for (k = n - 2 ; k >= 0 ; k--)
  {
    b[k] = y[k];
    for (j = k + 1 ; j < n ; j++)
    {
      b[k] -= a[k][j] * b[j];
    }
    b[k] /= a[k][k];
  }

  free(y);
  return(0);
}
