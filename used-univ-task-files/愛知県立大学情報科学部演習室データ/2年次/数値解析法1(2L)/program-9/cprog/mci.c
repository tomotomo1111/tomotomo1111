/*
モンテカルロ法による数値積分計算プログラム
*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/*
 矩形内での一様乱数ベクトル生成関数
 [入力]
   n: 次元
   a,b: 1次元配列（矩形: [a[0],b[0]] X [a[1],b[1]] X ... X [a[n-1],b[n-1]]）
 [出力]
   x: 1次元配列（n次元乱数ベクトル）
*/
void getrandvec(int n, double *a, double *b, double *x)
{
  int i;

  for(i = 0; i < n; i++) x[i] = a[i] + (double)rand()/RAND_MAX * (b[i] - a[i]);
  
  return;
}

/*
 モンテカルロ法による数値積分計算関数（直積集合にのみ対応）
 [入力]
  n: 空間次元
  a,b: 1次元配列（矩形: [a[0],b[0]] X [a[1],b[1]] X ... X [a[n-1],b[n-1]]）
  m: 積分点数
  f: 関数ポインタ (引数: int型とdoubleポインタ)
 [返却値]
  数値積分値
*/
double mcint(int n, double *a, double *b, int m, double (* f)(int,double *))
{
  int i;
  double mcval = 0.0, vvol = 1.0;
  double **x;

  x = (double **)calloc(m,sizeof(double *));
  if(x == NULL)
  {
    printf("領域の確保に失敗\n");
    exit(1);
  }
  for(i = 0; i < m; i++)
  {
    x[i] = (double *)calloc(n,sizeof(double));
    if(x[i] == NULL)
    {
      printf("領域の確保に失敗\n");
      exit(1);
    }
  }

  /* 分点の計算 */
  srand(time(NULL));
  for(i = 0; i < m; i++) getrandvec(n,a,b,x[i]);

  /* 矩形面積の計算 */
  for(i = 0; i < n; i++) vvol *= (b[i]-a[i]);

  /* [演習] モンテカルロ法による数値積分計算関数を完成させてください */
  
  for(i = 0; i < m ; i++) free(x[i]);
  free(x);

  return(mcval);
}
