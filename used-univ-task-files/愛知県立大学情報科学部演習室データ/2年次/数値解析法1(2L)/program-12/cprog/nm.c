/*
ニュートン法プログラム
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

/* 相対許容誤差 注) エラーが出る人は，それぞれの関数内に移動させてください．*/
const double epsr = 8.0*DBL_EPSILON;
/* const double epsr = FLT_EPSILON; */

/* 演習問題：真の解 (6,17ページ) */
double truex1d = 5.0e-1;

/* 演習問題：真の解ベクトル (13ページ) */
double truex2d[2] = {5.0e-1,2.5e-1};

/* ノルムなどの計算：lacalc.c の関数を使用 */
double vecnorm(int n, double *veca, int type);
double matnorm(int m, int n, double **matA, int type);

/* 連立一次方程式求解 */
void gausselm(int n, double **a, double *b);

/*
  1次元ニュートン法による求解関数
  [入力]
    x: 初期値
    f: 関数ポインタ (f(x) = 0を求める関数)
    df: 関数ポインタ (fの1階導関数)
  [出力]
    x: 近似解
  [返却値]
    プログラム終了状態 (0 -> 正常終了 (収束),  1 -> 異常終了 (最大反復回数到達))
  注) x はポインタ変数のため，使用する場合は *x としてください（詳しくは「参照渡し」で調べてください）
*/
int newton1d(double *x, double (* f)(double), double (* df)(double))
{
  int i,k = 0;
  double error,oldnorm,newnorm,sumnorm;
  double newx,dval;

  double err,olderr;
  FILE *fp;
  fp = fopen("exc-nm1d.dat","w");
  olderr = fabs(truex1d - *x);
  /* fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e\n",k,*x,olderr,log(olderr)); */
  fprintf(fp,"%4d  %17.15e  %17.15e\n",k,*x,olderr);

  oldnorm = fabs(*x);

  do
  {
    dval = df(*x);
    if (fabs(dval) <= epsa)
    {
      printf("Newton method (1D): Value of derivative is too small...\n");
      fclose(fp);
      return(1);
    }

    /* [演習] 1次元ニュートン法による求解関数を完成させてください */

    k++;

    if (k == ITERMAX){
      printf("Not converged ...\n");
      fclose(fp);
      return(1);
    }

    err = fabs(*x - truex1d);
    /* fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e  %17.15e\n",k,*x,err,log(err),err/olderr); */
    fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e\n",k,*x,err,err/olderr);
    olderr = err;
  }while (error >= epsa + epsr * sumnorm);

  printf("Converged: num. of iterations = %d\n",k);

  fclose(fp);
  
  return(0);
}

/*
  多次元ニュートン法による求解プログラム
  [入力]
    n: 次元数
    x: 1次元配列 (n次元, 初期ベクトル)
    f: 関数ポインタ (f(x) = 0を求める関数)
    jf: 関数ポインタ (f のヤコビ行列を求める関数)
  [出力]
    x: 1次元配列 (n次元, 解ベクトル)
  [返却値]
    プログラム終了状態 (0 -> 正常終了 (収束),  1 -> 異常終了 (最大反復回数到達))
  注) 関数fはベクトル値関数のため，f(次元,x,関数値を格納した配列)を想定しています．
  注) 関数jfは行列値関数のため，f(次元,x,ヤコビ行列を格納した2次元配列)を想定しています．
*/
int newtonmd(int n, double *x, void (* f)(int,double *,double *), void (* df)(int,double *,double **))
{
  int i,k = 0;
  double error,oldnorm,newnorm,sumnorm,jnorm;
  double *newx,*fvec;
  double **jmat;

  double err,olderr,errvec[2];
  FILE *fp;
  fp = fopen("exc-nm2d.dat","w");
  for(i = 0; i < n; i++) errvec[i] = truex2d[i] - x[i];
  olderr = vecnorm(n,errvec,3);
  /* fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e  %17.15e\n",k,x[0],x[1],olderr,log(olderr)); */
  /* fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e\n",k,x[0],x[1],olderr); */

  newx = (double *)calloc(n,sizeof(double));
  fvec = (double *)calloc(n,sizeof(double));
  jmat = (double **)calloc(n,sizeof(double *));
  if(newx == NULL || fvec == NULL || jmat == NULL){
    printf("領域の確保に失敗\n");
    return(1);
  }
  for(i = 0; i < n; i++){
    jmat[i] = (double *)calloc(n,sizeof(double));
    if(jmat[i] == NULL){
      printf("領域の確保に失敗\n");
      exit(1);
    }
  }

  oldnorm = vecnorm(n,x,3);

  do{
    f(n,x,fvec);
    df(n,x,jmat);
    for(i=0;i<n;i++) fvec[i] *= -1.0;
    gausselm(n,jmat,fvec);

    /* [演習] 多次元ニュートン法による求解関数を完成させてください */

    k++;

    for(i = 0; i < n; i++) errvec[i] = truex2d[i] - x[i];
    err = vecnorm(n,errvec,3);
    /* fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e  %17.15e  %17.15e\n",k,x[0],x[1],err,log(err),err/olderr); */
    fprintf(fp,"%4d  %17.15e  %17.15e  %17.15e  %17.15e\n",k,x[0],x[1],err,err/olderr);
    olderr = err;
  }while (error >= epsa + epsr * sumnorm && k < ITERMAX);
  
  fclose(fp);
  
  free(newx);
  free(fvec);
  for(i = 0; i < n ; i++) free(jmat[i]);
  free(jmat);

  if (k == ITERMAX){
    printf("Not converged ...\n");
    return(1);
  }
  else{
    printf("Converged: num. of iterations = %d\n",k);
    return(0);
  }
}
