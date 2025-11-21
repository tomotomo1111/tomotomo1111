/*
このプログラムを使用する場合は，まずludecomp.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testlu testlu.c ludecomp.c lacalc.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testlu で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int ludecomp(int n, double **a, int *ipiv);
int lusolve(int n, double **a, int *ipiv, double *b);
void matpm(int m, int n, double **matA, double **matB, double a, double b, double **matC);
void vecpm(int n, double *veca, double *vecb, double a, double b, double *vecc);
void mulmatvec(int m, int n, double **matA, double *vecb, double *vecc);
double vecnorm(int n, double *veca, int type);
double matnorm(int m, int n, double **matA, int type);

/*
 演習問題確認プログラム（LU分解）
*/
int main(void)
{
  int n = 100;
  int i,j,rval;
  double *b, *exactx, *diffx;
  double **A, **exactLU, **diffLU;
  int *ipiv;
  double luerr,abserr,xnorm;

  b = (double *)calloc(n,sizeof(double));
  if(b == NULL)
  {
    printf("領域の確保に失敗（右辺ベクトル）\n");
    exit(1);
  }

  exactx = (double *)calloc(n,sizeof(double));
  if(exactx == NULL)
  {
    printf("領域の確保に失敗（真の解ベクトル）\n");
    exit(1);
  }

  diffx = (double *)calloc(n,sizeof(double));
  if(diffx == NULL)
  {
    printf("領域の確保に失敗（誤差ベクトル）\n");
    exit(1);
  }

  A = (double **)calloc(n,sizeof(double *));
  if(A == NULL)
  {
    printf("領域の確保に失敗（係数行列）\n");
    exit(1);
  }
  for(i = 0; i < n; i++)
  {
    A[i] = (double *)calloc(n,sizeof(double));
    if(A[i] == NULL)
    {
      printf("領域の確保に失敗（係数行列）\n");
      exit(1);
    }
  }

  ipiv = (int *)calloc(n,sizeof(int));
  if(ipiv == NULL)
  {
    printf("領域の確保に失敗（ピボット選択結果配列）\n");
    exit(1);
  }

  exactLU = (double **)calloc(n,sizeof(double *));
  if(exactLU == NULL)
  {
    printf("領域の確保に失敗（LU分解）\n");
    exit(1);
  }
  for(i = 0; i < n; i++)
  {
    exactLU[i] = (double *)calloc(n,sizeof(double));
    if(exactLU[i] == NULL)
    {
      printf("領域の確保に失敗（LU分解）\n");
      exit(1);
    }
  }

  diffLU = (double **)calloc(n,sizeof(double *));
  if(diffLU == NULL)
  {
    printf("領域の確保に失敗（LU分解の誤差行列）\n");
    return 1;
  }
  for(i = 0; i < n; i++)
  {
    diffLU[i] = (double *)calloc(n,sizeof(double));
    if(diffLU[i] == NULL)
    {
      printf("領域の確保に失敗（LU分解の誤差行列）\n");
      exit(1);
    }
  }

  /* 真の解ベクトルの定義 */
  for(i = 0; i < n; i++) exactx[i] = 1.1;

  /* 行列の定義 */
  for(i = 0; i < n; i++)
  {
    for(j = 0; j < n; j++)
    {
      A[i][j] = fmin(i+1,j+1);
    }
  }

  /* 真のLU分解結果 */
  for(i = 0; i < n; i++)
  {
    for(j = 0; j < n; j++)
    {
      exactLU[i][j] = 1.0;
    }
  }

  /* 右辺ベクトルの計算 */
  mulmatvec(n,n,A,exactx,b);

  /* 係数行列のLU分解 */
  rval = ludecomp(n,A,ipiv);
  if (rval != 0) exit(1);

  /* 計算結果の確認 */
  matpm(n,n,A,exactLU,1.0,-1.0,diffLU);
  luerr = matnorm(n,n,diffLU,3);
  printf("Error (LU decomp., Frobenius norm) = %e\n", luerr);

  /* LU分解済み係数行列を用いた求解 */
  rval = lusolve(n,A,ipiv,b);
  if (rval != 0) exit(1);
  
  /* 相対誤差の計算 */
  xnorm = vecnorm(n,exactx,2);
  vecpm(n,b,exactx,1.0,-1.0,diffx);
  abserr = vecnorm(n,diffx,2);
  printf("Relative error (linear eq.) = %e\n", abserr/xnorm);

  free(b);
  free(exactx);
  free(diffx);
  for(i = 0; i < n ; i++)
  {
    free(A[i]);
    free(exactLU[i]);
    free(diffLU[i]);
  }
  free(A);
  free(exactLU);
  free(diffLU);

  return(0);
}
