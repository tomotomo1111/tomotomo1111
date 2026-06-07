/*
コレスキー分解による求解プログラム
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* 行列のコレスキー分解関数
  [入力]
    n: 行列・ベクトルの次元
    a: 2次元配列（係数行列）※対称なn次正方行列である必要あり
  [出力]
    s: 2次元配列（コレスキー分解行列）
*/
void chdecomp(int n, double **a, double **s)
{
  /* [演習] コレスキー分解関数を完成させてください． */
}

/* コレスキー法による求解関数
  [入力]
    n: 行列・ベクトルの次元
    a: 2次元配列（係数行列）※n次正方行列である必要あり
    b: 1次元配列（右辺ベクトル）
  [出力]
    b: 1次元配列（近似解ベクトル）
  [返却値]
    終了状態：0->正常終了, 1->異常終了
  注) 右辺ベクトルをとっておきたい場合は，修正が必要
*/
int cholesky(int n, double **a, double *b)
{
  int i,j,k;
  double **s;

  s = (double **)calloc(n,sizeof(double *));
  if(s == NULL)
  {
    printf("領域の確保に失敗（コレスキー法）\n");
    return(1);
  }
  for(i = 0; i < n; i++)
  {
    s[i] = (double *)calloc(n,sizeof(double));
    if(s[i] == NULL)
    {
      printf("領域の確保に失敗（コレスキー法）\n");
      return(1);
    }
  }

  /* コレスキー分解 */
  chdecomp(n,a,s);

  /* 前進代入 */
  b[0] /= s[0][0];
  for (i = 1 ; i < n ; i++)
  {
    for (k = 0 ; k <= i - 1 ; k++)
    {
      b[i] -= s[i][k] * b[k];
    }
    b[i] /= s[i][i];
  }
  b[n-1] /= s[n-1][n-1];

  /* 後退代入 */
  for (i = n - 2 ; i >= 0 ; i--)
  {
    for (k = i + 1 ; k < n ; k++)
    {
      b[i] -= s[k][i] * b[k];
    }
    b[i] /= s[i][i];
  }

  for(i = 0; i < n ; i++)
  {
    free(s[i]);
  }
  free(s);

  return(0);
}
