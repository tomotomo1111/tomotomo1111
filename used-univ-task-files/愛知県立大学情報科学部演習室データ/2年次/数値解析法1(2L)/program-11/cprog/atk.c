/*
逐次反復法+エイトケン加速プログラム
*/
#include <stdio.h>
#include <stdbool.h>
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

/* 演習問題：真の解 (23ページ) */
double truex = 5.0e-1;

/*
  逐次反復法+エイトケン加速による求解プログラム
  [入力]
    x: ポインタ変数 (初期値)
    f: 関数ポインタ (f(x) = 0を求める関数)
    bacc: 加速フラグ (bacc = true -> エイトケン加速あり, = false -> 加速なし（逐次近似法）)
  [出力]
    x: ポインタ変数 (近似解)
  [返却値]
    プログラム終了状態 (0 -> 正常終了 (収束),  1 -> 異常終了 (最大反復回数到達))
  注) x はポインタ変数のため，使用する場合は *x としてください（詳しくは「参照渡し」で調べてください）
*/
int atkmethod(double *x, double (* f)(double), bool bacc)
{
  int i,k = 0;
  double error,oldnorm,newnorm,sumnorm;
  double xa[3]; /* エイトケン加速用 */
  double y,ynew;

  if (bacc == false)
  {
    /* 逐次近似法のみ */
    y = *x;
  }
  else
  {
    /* 逐次近似法+エイトケン加速 */
    xa[0] = *x;
    xa[1] = *x - f(*x);
    xa[2] = xa[1] - f(xa[1]);
    y = xa[0] - (xa[1] - xa[0]) * (xa[1] - xa[0]) / (xa[2] - 2 * xa[1] + xa[0]);
  }
  
  double err,olderr;
  FILE *fp;
  if(bacc==false)
  {
    fp = fopen("exc-si.dat","w");
  }
  else
  {
    fp = fopen("exc-atk.dat","w");
  }
  olderr = fabs(y - truex);
  oldnorm = fabs(y);

  /* 逐次近似法 */
  if (bacc == false){
    do{
      ynew = y - f(y);
      newnorm = fabs(ynew);
      sumnorm = oldnorm + newnorm;
      oldnorm = newnorm;
      error = fabs(ynew - y);
      y = ynew;
      
      k++;
      *x = y;

      err = fabs(y - truex);
      /* fprintf(fp,"%4d  %17.15e  %17.15e  %16.15e\n",k,y,log(err),err/olderr); */
      fprintf(fp,"%4d  %17.15e  %17.15e  %16.15e\n",k,y,err,err/olderr);
      olderr = err;
    }while (error >= epsa + epsr * sumnorm && k < ITERMAX);
  } /* エイトケン加速 */
  else {
    do{
      /* [演習] エイトケン加速による求解関数を完成させてください */
      k++;
      *x = y;

      err = fabs(y - truex);
      /* fprintf(fp,"%4d  %17.15e  %17.15e  %16.15e\n",k,y,log(err),err/olderr); */
      fprintf(fp,"%4d  %17.15e  %17.15e  %16.15e\n",k,y,err,err/olderr);
      olderr = err;
    }while (error >= epsa + epsr * sumnorm && k < ITERMAX);
  }

  fclose(fp);

  if (k == ITERMAX){
       printf("Not converged ...\n");
       return(1);
  }
  else if (bacc == false){
    printf("Converged (simple iteration): num. of iterations = %d\n",k);
  }
  else{
    printf("Converged (Aitken): num. of iterations = %d\n",k);
  }

  return(0);
}
