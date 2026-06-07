/*
ライブラリ BLAS・LAPACK を使用したLU分解計算プログラムです．
コンパイルしてください．

[UbuntuなどLinuxの場合]
gcc -o testlu testlu_lapack.c -llapacke -llapack -lblas -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testlu -I(BLASインストールディレクトリ)/include -I(LAPACKインストールディレクトリ)/include -L(BLASインストールディレクトリ)/lib -L(LAPACKインストールディレクトリ)/lib testlu_lapack.c -llapacke -llapack -lblas -lm

[Windows（MSYS2+MinGW+OpenBLAS+LAPACK）の場合] 注) 追加
gcc -o testlu testlu_lapack.c -llapacke -llapack -lcblas -lm

コンパイルに成功した場合は，./testlu で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <cblas.h>
#include <lapacke.h>

/*
 演習問題プログラム（LU分解）
 */
int main(void)
{
  int n = 100;
  int i,j,rval;
  double *exactx,*b;
  double *A; // LAPACK, BLAS を使用する場合は1次元配列にした方が便利
  double relerr,xnorm;
  int *ipiv;

  exactx = (double *)calloc(n,sizeof(double));
  if(exactx == NULL)
  {
    printf("領域の確保に失敗（真の解ベクトル）\n");
    exit(1);
  }
  
  b = (double *)calloc(n,sizeof(double));
  if(b == NULL)
  {
    printf("領域の確保に失敗（右辺ベクトル）\n");
    exit(1);
  }

  A = (double *)calloc(n * n,sizeof(double));
  if(A == NULL)
  {
    printf("領域の確保に失敗（係数行列）\n");
    exit(1);
  }

  ipiv = (int *)calloc(n, sizeof(int));
  if(ipiv == NULL)
  {
    printf("領域の確保に失敗（ピボット選択結果配列）\n");
    exit(1);
  }
  
  /* 真の解ベクトルの定義 */
  for(i = 0; i < n; i++) exactx[i] = 1.1;

  /* 行列の定義 (行優先方式の格納) */
  for(i = 0; i < n; i++)
  {
    for(j = 0; j < n; j++)
    {
      A[i*n + j] = fmin(i+1,j+1);
    }
  }

  /* 行列ベクトル積 A*exactx の計算 */
  cblas_dgemv(CblasRowMajor,CblasNoTrans,n,n,1.0,A,n,exactx,1,0.0,b,1);

  /* 真の解ベクトルのノルム計算 */
  xnorm = cblas_dnrm2(n,exactx,1);

  /* LU分解 */
  rval = LAPACKE_dgetrf(LAPACK_ROW_MAJOR,n,n,A,n,ipiv);
  if(rval != 0) exit(1);

  /* 前進・後退代入 */
  rval = LAPACKE_dgetrs(LAPACK_ROW_MAJOR,'N',n,1,A,n,ipiv,b,1);
  if(rval != 0) exit(1);
  
  /* 誤差ベクトルの計算（exactxへ上書き）*/
  cblas_daxpy(n,-1.0,b,1,exactx,1);

  /* 相対誤差の計算 */
  relerr = cblas_dnrm2(n,exactx,1) / xnorm;
  printf("Relative error = %e\n", relerr);

  free(exactx);
  free(b);
  free(A);
  free(ipiv);

  return(0);
}
