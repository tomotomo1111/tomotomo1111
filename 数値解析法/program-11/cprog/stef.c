/*
ステファンセンプログラム
*/
#include <stdio.h>
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

/* 演習問題：真の解 (23,31ページ) */
double truex = 5.0e-1;

/*
  ステファンセン反復用同値変形関数
  [入力]
    xp: 計算する点
    f: 関数ポインタ
  [返却値]
    x - f(x) の値
*/
double gfunc(double xp,double (* f)(double))
{
  return(xp-f(xp));
}

/*
  ステファンセンによる求解プログラム
  [入力]
    x: ポインタ変数 (初期値)
    f: 関数ポインタ (f(x) = 0を求める関数)
  [出力]
    x: ポインタ変数 (近似解)
  [返却値]
    プログラム終了状態 (0 -> 正常終了 (収束),  1 -> 異常終了 (最大反復回数到達))
  注) x はポインタ変数のため，使用する場合は *x としてください（詳しくは「参照渡し」で調べてください）
*/
int stefmethod(double *x, double (* f)(double))
{
  int i,k = 0;
  double error,oldnorm,newnorm,sumnorm;
  double g1,g2;
  double xnew;

  double err,olderr;
  FILE *fp;
  fp = fopen("exc-stf.dat","w");
  olderr = fabs(*x - truex);
  oldnorm = fabs(*x);

  do{
    /* [演習] ステファンセン反復による求解関数を完成させてください */

    k++;

    err = fabs(*x - truex);
    /* fprintf(fp,"%4d  %17.15e  %17.15e  %16.15e\n",k,*x,log(err),err/olderr); */
    fprintf(fp,"%4d  %17.15e  %17.15e  %16.15e\n",k,*x,err,err/olderr);
    olderr = err;
  }while (error >= epsa + epsr * sumnorm && k < ITERMAX);

  fclose(fp);

  if (k == ITERMAX){
    printf("Not converged ...\n");
    return(1);
  }
  else{
    printf("Converged (Steffensen): num. of iterations = %d\n",k);
    return(0);
  }
}
