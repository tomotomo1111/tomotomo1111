/*
 最小二乗近似プログラム
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void mulmatvec(int m, int n, double **matA, double *vecb, double *vecc);
void mulmatmat(int m, int nab, int n, double **matA, double **matB, double **matC);
void gausselm(int n, double **a, double *b);

/*
  係数計算プログラム
  [入力]
    n: 分点数-1 (スライドp.26参照)
    m: 近似多項式次元
    x: 1次元配列 ((n+1)次元, 関数値が与えられた点)
    f: 1次元配列 ((n+1)次元, 関数値)
  [出力]
    c: 1次元配列 ((m+1)次元, 係数)
  [返却値]
    終了状態：0->正常終了, 1->異常終了 (正規方程式の解が一意でない)
*/
int lsqcoef(int n, int m, double *x, double *f, double *c)
{
  int i,j,k,l;
  double **A,**At,**AtA;

  A = (double **)calloc(n+1,sizeof(double *));
  At = (double **)calloc(m+1,sizeof(double *));
  AtA = (double **)calloc(m+1,sizeof(double *));
  if(A == NULL || At == NULL || AtA == NULL)
  {
    printf("領域の確保に失敗（最小二乗近似）\n");
    exit(1);
  }
  for(i = 0; i <= n; i++)
  {
    A[i] = (double *)calloc(m+1,sizeof(double));
    if(A[i] == NULL)
    {
      printf("領域の確保に失敗（最小二乗近似）\n");
      exit(1);
    }
  }
  for(i = 0; i <= m; i++)
  {
    At[i] = (double *)calloc(n+1,sizeof(double));
    AtA[i] = (double *)calloc(m+1,sizeof(double));
    if(At[i] == NULL || AtA[i] == NULL)
    {
      printf("領域の確保に失敗（最小二乗近似）\n");
      exit(1);
    }
  }
  
  /* 行列 A 各要素の格納 */
  for(i = 0 ; i <= n ; i++)
  {
    for (j = 0 ; j <= m ; j++)
    {
	A[i][j] = pow(x[i], j);
    }
  }

  /* [演習] 27ページを参考に，最小二乗近似の係数 c_i を計算する関数を完成させてください．*/
  
  /* 部分ピポット選択付きガウスの消去法による求解 */
  gausselm(m+1,AtA,c);
  
  for(i = 0; i <= n ; i++) free(A[i]);
  free(A);
  for(i = 0; i <= m ; i++){
    free(At[i]);
    free(AtA[i]);
  }
  free(At);
  free(AtA);
  
  return(0);
}

/*
  最小二乗近似計算プログラム
  [入力]
    m: 近似多項式次元
    c: 1次元配列 ((m+1)次元, 係数)
    xp: 近似値を計算する点
  [返却値]
    xpにおける近似値
  注) 事前にlsqcoef関数を実行し，係数を求めておく必要がある
*/
double lsqcalc(int m, double *c, double xp)
{
  /* [演習] 最小二乗近似計算プログラムを作成してください */
}
