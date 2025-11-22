/*
逐次反復法プログラム
*/
#include <stdio.h>
#include <stdlib.h>
#include <float.h>
#include <math.h>

/* 最大反復回数 */
const int ITERMAX = 1000;

/* 絶対許容誤差 */
const double epsa = DBL_MIN;
/* const double epsa = FLT_MIN; */

/* 相対許容誤差 */
const double epsr = 8.0*DBL_EPSILON;
/* const double epsr = FLT_EPSILON; */

/* 演習問題：真の解ベクトル (13ページ) */
double truex[2] = {5.0e-1,2.5e-1};

/* ノルムなどの計算：lacalc.c の関数を使用 */
double vecnorm(int n, double *veca, int type);

/*
  逐次反復法による求解プログラム
  [入力]
    n: 次元数
    x: 1次元配列 (n次元, 初期ベクトル)
    f: 関数ポインタ (f(x) = 0を求める関数)
  [出力]
    x: 1次元配列 (n次元, 解ベクトル)
  [返却値]
    プログラム終了状態 (0 -> 正常終了 (収束),  1 -> 異常終了 (最大反復回数到達))
  注) 関数fはベクトル値関数のため，f(次元,x,関数値を格納した配列)を想定しています．
*/
int simethod(int n, double *x, void (* f)(int,double *,double *))
{
  int i,k = 0;
  double error,oldnorm,newnorm,sumnorm;
  double *newx,*fvec;

  /* 演習解答のみで必要 */
  double err,olderr,errvec[2];
  FILE *fp;
  fp = fopen("exc-si2d.dat","w");
  for(i = 0; i < n; i++) errvec[i] = truex[i] - x[i];
  olderr = vecnorm(n,errvec,3);

  newx = (double *)calloc(n,sizeof(double));
  fvec = (double *)calloc(n,sizeof(double));
  if(newx == NULL || fvec == NULL)
  {
    printf("領域の確保に失敗\n");
    return(1);
  }

  oldnorm = vecnorm(n,x,3);

  do
  {
    f(n,x,fvec);
    for(i = 0 ; i < n ; i++)
    {
      newx[i] = x[i] - fvec[i];
    }
    /* 更新ベクトルのノルム計算 */
    newnorm = vecnorm(n,newx,3);
    /* ベクトルノルム値の和 */
    sumnorm = oldnorm + newnorm;
    oldnorm = newnorm;
    /* ||x_new - x||_infty の計算 */
    error = vecnorm(n,fvec,3);

    for (i = 0 ; i < n ; i++)
    {
      x[i] = newx[i];
    }
    k++;

    /* 絶対誤差計算（例題のみ必要） */
    for(i = 0; i < n; i++) errvec[i] = truex[i] - x[i];
    err = vecnorm(n,errvec,3);
    //fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e  %16.15e\n",k,x[0],x[1],log(err),err/olderr);
    fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e  %16.15e\n",k,x[0],x[1],err,err/olderr);
    olderr = err;
  }while (error >= epsa + epsr * sumnorm && k < ITERMAX);

  fclose(fp);
  free(newx);
  free(fvec);

  if (k == ITERMAX){
    printf("Not converged ...\n");
    return(1);
  }
  else{
    printf("Converged (simple iteration): num. of iterations = %d\n",k);
    return(0);
  }
}
