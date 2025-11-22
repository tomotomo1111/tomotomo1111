/*
注) 文字コードはUTF-8，改行コードはLFです．

このプログラムを使用する場合は，まずlacalc.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済) の場合]
gcc -o testnorm testnorm.c lacalc.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testnorm で実行できます．
実行結果を確認してください．

[実行結果]
vector:
 1 norm = 5.0500000000000000e+03
 2 norm = 5.8167860541711525e+02
 max norm = 1.0000000000000000e+02

matrix:
 1 norm = 5.0500000000000000e+03
 Frobenius norm = 4.1235118527779205e+03
 max norm = 5.0500000000000000e+03
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double vecnorm(int n, double *veca, int type);
double matnorm(int m, int n, double **matA, int type);

/*
 演習問題確認プログラム（ベクトル・行列ノルム）
 */
int main(void)
{
  int m = 100, n = 100;
  int i,j;
  double *x;
  double **A;
  double xnorm1, xnorm2, xnorminf, Anorm1, Anormfro, Anorminf;

  x = (double *)calloc(n,sizeof(double));
  if(x == NULL)
  {
    printf("領域の確保に失敗（ベクトル）\n");
    exit(1);
  }

  A = (double **)calloc(m,sizeof(double *));
  if(A == NULL)
  {
    printf("領域の確保に失敗（行列）\n");
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

  /* ベクトルの定義 */
  for(i = 0; i < n; i++) x[i] = i + 1;

  /* 行列の定義 */
  for(i = 0; i < m; i++)
  {
    for(j = 0; j < n; j++)
    {
      A[i][j] = fmin(i+1,j+1);
    }
  }

  /* ベクトルノルムの計算 */
  xnorm1 = vecnorm(n,x,1);
  xnorm2 = vecnorm(n,x,2);
  xnorminf = vecnorm(n,x,3);
  printf("vector:\n 1 norm = %18.16e\n 2 norm = %18.16e\n max norm = %18.16e\n\n", xnorm1, xnorm2, xnorminf);

  /* 行列ノルムの計算 */
  Anorm1 = matnorm(m,n,A,1);
  Anormfro = matnorm(m,n,A,3);
  Anorminf = matnorm(m,n,A,4);
  printf("matrix:\n 1 norm = %18.16e\n Frobenius norm = %18.16e\n max norm = %18.16e\n", Anorm1, Anormfro, Anorminf);

  free(x);
  for(i = 0; i < m ; i++) free(A[i]);
  free(A);

  return(0);
}
