/* ノルム計算関数のテスト:
    testnorm.cを利用すると便利です（詳しくはtestnorm.cのコメントを参照）
*/
#include <stdio.h>
#include <math.h>

/* ベクトル演算 a*veca + b*vecb の計算
  [入力]
    n: 次元
    veca,vecb: 1次元配列（ベクトル）
    a,b: 係数
  [出力]
    vecc: a*veca + b*vecb を格納した1次元配列
  [返却値]
    なし
*/
void vecpm(int n, double *veca, double *vecb, double a, double b, double *vecc)
{
  int i;

  for(i = 0; i < n; i++) vecc[i] = a*veca[i] + b*vecb[i];

  return;
}

/* 行列演算 a*matA + b*matB の計算
  [入力]
    m: 行数
    n: 列数
    matA,matB: 2次元配列（行列）
    a,b: 係数
  [出力]
    matC: a*matA + b*matB を格納した2次元配列
  [返却値]
    なし
*/
void matpm(int m, int n, double **matA, double **matB, double a, double b, double **matC)
{
  int i,j;

  for(i = 0; i < m; i++)
  {
    for(j = 0; j < n; j++)
    {
      matC[i][j] = a*matA[i][j] + b*matB[i][j];
    }
  }
  return;
}

/* 行列ベクトル積の計算
  [入力]
    m: matAの行数
    n: matAの列数, bの次元
    matA: 2次元配列（行列）
    vecb: 1次元配列（ベクトル）
  [出力]
    vecc: matA*vecb を格納した1次元配列
  [返却値]
    なし
*/
void mulmatvec(int m, int n, double **matA, double *vecb, double *vecc)
{
  int i,j;

  for(i = 0; i < m; i++)
  {
    vecc[i] = 0.0;
    for(j = 0; j < n; j++)
    {
      vecc[i] += matA[i][j] * vecb[j];
    }
  }

  return;
}

/* 行列積A*Bの計算
  [入力]
    m: matAの行数
    nab: matAの列数, matBの行数
    n: matBの列数
    matA,matB: 2次元配列（行列）
  [出力]
    matC: matA*matB を格納した2次元配列
  [返却値]
    なし
*/
void mulmatmat(int m, int nab, int n, double **matA, double **matB, double **matC)
{
  int i,j,k;

  for(i = 0; i < m; i++)
  {
    for(j = 0; j < n; j++)
    {
      matC[i][j] = 0.0;
      for(k = 0; k < nab; k++) matC[i][j] += matA[i][k]*matB[k][j];
    }
  }

  return;
}

/* ベクトル内積a^t bの計算
  [入力]
    n: ベクトルの次元
    veca,vecb: 1次元配列（ベクトル）
  [返却値]
    内積の値
*/
double vecdot(int n, double *veca, double *vecb)
{
  int i;
  double dval = 0.0;

  for(i = 0; i < n; i++) dval += veca[i]*vecb[i];

  return(dval);
}

/* ベクトルノルムの計算
  [入力]
    n: ベクトルの次元
    veca: 1次元配列（ベクトル）
    type: type = 1 -> 1ノルム; type = 2 -> 2ノルム; それ以外 -> 最大値ノルム
  [返却値]
    ノルムの値
*/
double vecnorm(int n, double *veca, int type)
{
  int i;
  double val = 0.0; /* ノルムの値を格納するための変数です */

  if(type == 1)
  {
    for(i = 0; i < n; i++) val += fabs(veca[i]);
  }
  else if(type == 2)
  {
    val = sqrt(vecdot(n, veca, veca));
  }
  else
  {
    val = veca[0];
    for(i = 1; i < n; i++) {
      if(veca[i] > val) val == fabs(veca[i]);
    }
  }

  return(val);
}

/* 行列ノルムの計算
  [入力]
    m: 行数
    n: 列数
    matA: 2次元配列（行列）
    type: type = 1 -> 1ノルム; type = 3 -> フロベニウスノルム; それ以外 -> 最大値ノルム
  [返却値]
    行列ノルムの値
  注) 2ノルムは実装しません．
*/
double matnorm(int m, int n, double **matA, int type)
{
  int i,j;
  double mval=0.0; /* ノルムの値を格納するための変数です */
  double mtmp;

  if(type == 1)
  {
    /* [演習] 行列ノルム（1ノルム）の計算コードを記述してください */
  }
  else if(type == 3)
  {
    /* フロベニウスノルム */
    for(i = 0; i < m; i++)
    {
      for(j = 0; j < n; j++)
      {
	mval += pow(fabs(matA[i][j]),2.0);
      }
    }
    mval = sqrt(mval);
  }
  else
  {
    /* [演習] 行列ノルム（最大値ノルム）の計算コードを記述してください */
  }

  return(mval);
}
