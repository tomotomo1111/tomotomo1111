/*
 3次スプライン補間プログラム
*/
#include <stdio.h>
#include <stdlib.h>

/*
  狭義優対角三重対角行列用ガウスの消去法プログラム
  [入力]
    n: 行列次数
    alpha: 1次元配列 (下三角部分成分)
    beta:  1次元配列 (対角成分)
    gamma: 1次元配列 (上三角部分)
    b: 1次元配列 (右辺ベクトル)
  [出力]
    b: 1次元配列 (近似解ベクトル)
*/
void gausstrig(int n, double *alpha, double *beta, double *gamma, double *b)
{
  int k;

  /* 前進消去過程 */
  for (k = 0 ; k < n - 1 ; k++)
  {
    alpha[k] /= beta[k];
    beta[k+1] -= alpha[k] * gamma[k];
    b[k+1] -= alpha[k] * b[k];
  }

  /* 後退代入 */
  b[n-1] /= beta[n-1];
  for (k = n - 2 ; k >= 0 ; k--)
  {
    b[k] -= gamma[k] * b[k + 1];
    b[k] /= beta[k];
  }

  return;
}

/* 
  3次スプライン補間係数計算関数
  [入力]
    n: 補間点数-1 (スライドp.9参照)
    x: 1次元配列 ((n+1)次元, 分点座標)
    f: 1次元配列 ((n+1)次元, 分点における関数値)
    df: 1次元配列 (サイズ2, 端点の導関数値)
  [出力]
    c: 1次元配列 ((n+1)次元, 補間係数)
  [返却値]
    終了状態：0->正常終了, 1->異常終了
*/
int cscoef(int n, double *x, double *f, double *df, double *c)
{
  int i;
  double h1,h2;
  double *alpha, *beta, *gamma;

  alpha = (double *)calloc(n+1,sizeof(double));
  beta = (double *)calloc(n+1,sizeof(double));
  gamma = (double *)calloc(n+1,sizeof(double));
  if(alpha == NULL || beta == NULL || gamma == NULL)
  {
    printf("領域の確保に失敗（3次スプライン補間係数）\n");
    return(1);
  }

  /* [演習] 16ページを参考に，係数c_iを計算する関数を完成させてください． */

  gausstrig(n+1,alpha,beta,gamma,c);

  free(alpha);
  free(beta);
  free(gamma);

  return(0);
}

/* 
  1次スプライン補間による補間値計算関数
  [入力]
    n: 補間点数-1 (スライドp.6参照)
    x: 1次元配列 ((n+1)次元, 分点座標)
    f: 1次元配列 ((n+1)次元, 分点における関数値)
    xp; 補間値を求める点
  [返却値]
    xpにおける補間値
*/
double lspline(int n, double *x, double *f, double xp)
{
  int i, j;
  double rval;

  /* 小区間の探索 (線形探索：もっと高速なアルゴリズムを使ったほうが...) */
  for (j = 1 ; j < n ; j++)
  {
    if(xp >= x[j-1] && xp <= x[j])
    {
      i = j;
      break;
    }
    else if (j == n-1)
    {
      i = j;
    }
  }

  double h=x[i]-x[i-1];
  double xi=x[i]-xp;
  double xim=xp-x[i-1];
  rval = f[i-1] * xi / h + f[i] * xim / h;

  return(rval);
}

/* 
  3次スプライン補間による補間値計算関数
  [入力]
    n: 補間点数-1 (スライドp.9参照)
    x: 1次元配列 ((n+1)次元, 分点座標)
    f: 1次元配列 ((n+1)次元, 分点における関数値)
    c: 1次元配列 ((n+1)次元, 補間係数)
    xp; 補間値を求める点
  [返却値]
    xpにおける補間値
  注) 事前に関数cscoefを実行し補間係数cを求めておく必要がある
*/
double cspline(int n, double *x, double *f, double *c, double xp)
{
  int i, j;
  double rval;

  /* 小区間の探索 (線形探索：もっと高速なアルゴリズムを使ったほうが...) */
  for (j = 1 ; j < n ; j++)
  {
    if(xp >= x[j-1] && xp <= x[j])
    {
      i = j;
      break;
    }
    else if (j == n-1)
    {
      i = j;
    }
  }

  /* [演習] 14ページを参考に，補間値を計算する関数を完成させてください．なお，補間値は rval へ格納することを想定しています． */

  return(rval);
}
