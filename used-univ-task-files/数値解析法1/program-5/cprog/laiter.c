/*
ヤコビ法・ガウス・ザイデル法・SOR法プログラム
 */
#include <stdio.h>
#include <stdlib.h>
#include <float.h>
#include <math.h>

/* ヤコビ法・ガウス・ザイデル法・SOR法の最大反復回数 */
const int ITERMAX = 5000;

/* 絶対許容誤差 */
const double epsa = DBL_MIN;
/* const double epsa = FLT_MIN; */

/* 相対許容誤差 */
const double epsr = DBL_EPSILON;
/* const double epsr = FLT_EPSILON; */

/* ノルムなどの計算：lacalc.c の関数を使用 */
void vecpm(int n, double *veca, double *vecb, double a, double b, double *vecc);
double vecnorm(int n, double *veca, int type);

/* 
  ヤコビ法による求解関数
  [入力]
    n: 行列・ベクトルの次元
    a: 2次元配列（係数行列）※n次正方行列である必要あり
    b: 1次元配列（右辺ベクトル）
    x: 1次元配列（初期ベクトル）
  [出力]
    x: 1次元配列（近似解ベクトル）
  [返却値]
    終了状態：0->正常終了, 1->異常終了
*/
int jacobi(int n, double **a, double *b, double *x)
{
  int i,j,k = 0;
  double err,oldnorm,newnorm,sumnorm;
  double *newx,*errvec;
  FILE *fp;

  newx = (double *)calloc(n,sizeof(double));
  errvec = (double *)calloc(n,sizeof(double));
  if(newx == NULL || errvec == NULL)
  {
    printf("領域の確保に失敗（ヤコビ法）\n");
    return(1);
  }

  fp = fopen("jacobi.dat","w");
  oldnorm = vecnorm(n,x,2);

  do
  {
    for(i = 0 ; i < n ; i++)
    {
      newx[i] = b[i];
      for (j = 0 ; j < n ; j++)
      {
	if(j != i) newx[i] -= a[i][j] * x[j];
      }
      newx[i] /= a[i][i];
    }

    /* 更新ベクトルのノルム計算 */
    newnorm = vecnorm(n,newx,2);

    /* ベクトルノルム値の和 */
    sumnorm = oldnorm + newnorm;

    /* ベクトル間の差を計算 */
    vecpm(n,newx,x,1.0,-1.0,errvec);
    err = vecnorm(n,errvec,2);

    for (i = 0 ; i < n ; i++)
    {
      x[i] = newx[i];
    }
    oldnorm = newnorm;

    fprintf(fp,"%4d  %16.15e\n",k+1,err);

    /* 反復回数の更新と確認 */
    k++;
    if (k == ITERMAX)
    {
      printf("Jacobi method: Iteration number = ITERMAX...\n");
      printf("Jacobi method: err = %e\n\n",err);
      fclose(fp);
      return(1);
    }
  }while (err >= epsa + epsr * sumnorm);

  printf("Jacobi method: Convergence!! Iteration number = %d\n",k);

  fclose(fp);
  
  free(newx);
  free(errvec);
  
  return(0);
}

/* 
  ガウス・ザイデル法による求解関数
  [入力]
    n: 行列・ベクトルの次元
    a: 2次元配列（係数行列）※n次正方行列である必要あり
    b: 1次元配列（右辺ベクトル）
    x: 1次元配列（初期ベクトル）
  [出力]
    x: 1次元配列（近似解ベクトル）
  [返却値]
    終了状態：0->正常終了, 1->異常終了
*/
int gaussseidel(int n, double **a, double *b, double *x)
{
  int i,j,k = 0;
  double err,oldnorm,newnorm,sumnorm;
  double *newx,*errvec;
  FILE *fp;

  newx = (double *)calloc(n,sizeof(double));
  errvec = (double *)calloc(n,sizeof(double));
  if(newx == NULL || errvec == NULL)
  {
    printf("領域の確保に失敗（ガウス・ザイデル法）\n");
    return(1);
  }

  oldnorm = vecnorm(n,x,2);
  fp = fopen("gaussseidel.dat","w");

  do
  {
	/* [演習] ガウス・ザイデル法関数を完成させてください */

    fprintf(fp,"%4d  %16.15e\n",k+1,err);

    /* 反復回数の更新と確認 */
    k++;
    if (k == ITERMAX)
    {
      printf("Gauss-Seidel method: Iteration number = ITERMAX...\n");
      printf("Gauss-Seidel method: err = %e\n\n",err);
      fclose(fp);
      return(1);
    }
  }while (err >= epsa + epsr * sumnorm);

  printf("Gauss-Seidel method: Convergence!! Iteration number = %d\n",k);

  fclose(fp);
  
  free(newx);
  free(errvec);
  
  return(0);
}

/* 
  SOR法による求解関数
  [入力]
    n: 行列・ベクトルの次元
    a: 2次元配列（係数行列）※n次正方行列である必要あり
    b: 1次元配列（右辺ベクトル）
    w: 過大緩和パラメータ (0 < w < 2)
    x: 1次元配列（初期ベクトル）
  [出力]
    x: 1次元配列（近似解ベクトル）
  [返却値]
    終了状態：0->正常終了, 1->異常終了
*/
int sor(int n, double **a, double *b, double w, double *x)
{
  int i,j,k = 0;
  double err,oldnorm,newnorm,sumnorm;
  double *newx,*errvec;
  FILE *fp;

  if(w <= 0 || w >= 2.0)
  {
    printf("過大緩和パラメータは (0,2) の範囲で指定してください（SOR法）\n");
    return(1);
  }
  
  newx = (double *)calloc(n,sizeof(double));
  errvec = (double *)calloc(n,sizeof(double));
  if(newx == NULL || errvec == NULL)
  {
    printf("領域の確保に失敗（SOR法）\n");
    return(1);
  }

  fp = fopen("sor.dat","w");
  
  oldnorm = vecnorm(n,x,2);

  do
  {
	/* [演習] SOR法関数を完成させてください */

    fprintf(fp,"%4d  %16.15e\n",k+1,err);

    /* 反復回数の更新と確認 */
    k++;
    if (k == ITERMAX)
    {
      printf("SOR method: Iteration number = ITERMAX...\n");
      printf("SOR method: err = %e\n\n",err);
      fclose(fp);
      return(1);
    }
  }while (err >= epsa + epsr * sumnorm);

  printf("SOR method: Convergence!! Iteration number = %d\n",k);

  fclose(fp);
  
  free(newx);
  free(errvec);
  
  return(0);
}
