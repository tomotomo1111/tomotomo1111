/* ルンゲクッタ型解法プログラム */
#include <stdio.h>
#include <stdlib.h>

/* オイラー法関数
  [入力]
    ndiv: 分割数
    h: 分割幅 (=最終時刻/ndiv)
    u0: 初期値
    F: 関数ポインタ
  [出力]
    U: 1次元配列 (ndiv+1次元, 近似解)
*/
void euler(int ndiv, double h, double u0, double (* F)(double,double),double *U)
{
  int n;
  double t;

  /* [演習] オイラー法関数を完成させてください */

  return;
}

/* 陽的ルンゲクッタ法関数
  [入力]
    ndiv: 分割数
    h: 分割幅 (=最終時刻/ndiv)
    u0: 初期値
    s: 公式の段数
    a: ブッチャー配列a (2次元配列)
    b: ブッチャー配列b (1次元配列)
    c: ブッチャー配列c (1次元配列)
    F: 関数ポインタ
  [出力]
    U: 1次元配列 (ndiv+1次元, 近似解)
  [返却値]
    プログラム終了状態 (0 -> 正常終了,  1 -> 異常終了)
*/
int explicit_rk(int ndiv, double h, double u0, int s, double **a, double *b, double *c, double (* F)(double,double),double *U)
{
  int i,j,n;
  double t;
  double *k, ktmp;

  k = (double *)calloc(s, sizeof(double));
  if(k == NULL){
    printf("領域の確保に失敗\n");
    return(1);
  }

  /* [演習] s段目陽的ルンゲ・クッタ法関数を完成させてください */

  free(k);
  
  return(0);
}
