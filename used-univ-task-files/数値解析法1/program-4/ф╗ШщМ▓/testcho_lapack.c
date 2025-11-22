/*
ライブラリ BLAS・LAPACK を使用したコレスキー法計算プログラムです．
コンパイルしてください．

[UbuntuなどLinuxの場合]
gcc -o testcho testcho_lapack.c -llapacke -llapack -lblas -lm
注) 演習室WSではコンパイルできません

[OS Xの場合]
gcc -o testcho -I(BLASインストールディレクトリ)/include -I(LAPACKインストールディレクトリ)/include -L(BLASインストールディレクトリ)/lib -L(LAPACKインストールディレクトリ)/lib testcho_lapack.c -llapacke -llapack -lblas -lm

[Windows（MSYS2+MinGW+OpenBLAS+LAPACK）の場合] 注) 追加
gcc -o testcho testcho_lapack.c -llapacke -llapack -lcblas -lm

コンパイルに成功した場合は，./testcho で実行できます．
計算結果を確認してください．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <cblas.h>
#include <lapacke.h>

/*
 演習問題プログラム（コレスキー法）
 */
int main(void)
{
  int n = 100;
  int i,j,rval;
  double *exactx,*b;
  double *A; // LAPACK, BLAS を使用する場合は1次元配列にした方が便利
  double relerr,xnorm;

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

  /* 対称行列用 */
  A = (double *)calloc(n*(n+1)/2,sizeof(double));
  if(A == NULL)
  {
    printf("領域の確保に失敗（係数行列）\n");
    exit(1);
  }

  /* 真の解ベクトルの定義 (対称行列) */
  for(i = 0; i < n; i++) exactx[i] = 1.1;

  /* 行列の定義 (対称行列用の格納（パックド格納），上三角部分)
     ※この形式を使用する場合は，列優先形式となるので注意 */
  for(i = 0; i < n; i++)
  {
    for(j = i; j < n; j++)
    {
      A[i+j*(j+1)/2] = fmin(i+1,j+1);
    }
  }

  /* 対称行列（パックド格納）ベクトル積 A*exactx の計算 */
  cblas_dspmv(CblasColMajor,CblasUpper,n,1.0,A,exactx,1,0.0,b,1);

  /* 真の解ベクトルのノルム計算 */
  xnorm = cblas_dnrm2(n,exactx,1);

  /* コレスキー法（パックド格納用） */
  rval = LAPACKE_dppsv(LAPACK_COL_MAJOR,'U',n,1,A,b,n);
  if(rval != 0) exit(1);
  
  /* コレスキー分解の結果も必要な場合は，以下のとおりにすればよい */
  /* コレスキー分解（パックド格納用） */
  /* rval = LAPACKE_dpptrf(LAPACK_COL_MAJOR,'U',n,A);
     if(rval != 0) exit(1); */
  
  /* 前進・交代代入 */
  /* rval = LAPACKE_dpptrs(LAPACK_COL_MAJOR,'U',n,1,A,b,n);
     if(rval != 0) exit(1); */
  
  /* 誤差ベクトルの計算（exactxへ上書き）*/
  cblas_daxpy(n,-1.0,b,1,exactx,1);

  /* 相対誤差の計算 */
  relerr = cblas_dnrm2(n,exactx,1) / xnorm;
  printf("Relative error = %e\n", relerr);

  free(exactx);
  free(b);
  free(A);

  return(0);
}
