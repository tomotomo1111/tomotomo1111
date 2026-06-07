/*
このプログラムを使用する場合は，まずrkm.c内のexplicit_rk関数を完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testrkm24 testrkm24.c rkm.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testrkm24 で実行できます．
計算結果を確認してください．

[実行結果]
Max error:
h             Heun                    Classical RK          
1.000000e-01  3.732466747563556e-05  2.049930669656419e-09
1.000000e-02  1.751671488969819e-07  1.538769112130467e-13
1.000000e-03  2.243685570668674e-09  7.771561172376096e-16
1.000000e-04  2.292355194555284e-11  1.676436767183986e-14
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

int explicit_rk(int ndiv, double h, double u0, int s, double **a, double *b, double *c, double (* F)(double,double),double *U);

/* 真の解 */
double trueu(double t){
  return(exp(-t*t/2));
}

/* 例題関数 */
double exF(double t, double u){
  return(-t * u);
}

/*
 演習問題確認プログラム (陽的ルンゲ・クッタ法)
*/
int main(void){
  int i,n,ndiv;
  int rval=0,rval2=0,rval4=0;
  double h;
  double (* F)(double,double);
  double **a2,*b2,*c2;
  double **a4,*b4,*c4;

  /* 例題用設定 */
  int s2 = 2, s4 = 4;
  double hmin = 1.0e-4, tlast = 5.0e-1;
  int nmax = (int)(tlast / hmin), nitr = 4;
  double u0 = 1.0,t;
  double errrk2,maxerrrk2,errrk4,maxerrrk4;
  double *Urk2, *Urk4;
  FILE *fp,*fperr;

  Urk2 = (double *)calloc(nmax + 1,sizeof(double));
  Urk4 = (double *)calloc(nmax + 1,sizeof(double));
  a2 = (double **)calloc(s2,sizeof(double *));
  b2 = (double *)calloc(s2,sizeof(double));
  c2 = (double *)calloc(s2,sizeof(double));
  a4 = (double **)calloc(s4,sizeof(double *));
  b4 = (double *)calloc(s4,sizeof(double));
  c4 = (double *)calloc(s4,sizeof(double));
  if(Urk2 == NULL || Urk4 == NULL || a2 == NULL || b2 == NULL || c2 == NULL || a4 == NULL || b4 == NULL || c4 == NULL){
    printf("領域の確保に失敗\n");
    exit(1);
  }
  for(i = 0; i < s2; i++){
    a2[i] = (double *)calloc(s2,sizeof(double));
    if (a2[i] == NULL){
      printf("領域の確保に失敗\n");
      exit(1);
    }
  }
  for(i = 0; i < s4; i++){
    a4[i] = (double *)calloc(s4,sizeof(double));
    if (a4[i] == NULL){
      printf("領域の確保に失敗\n");
      exit(1);
    }
  }

  /* ブッチャー配列 (ホイン法) */
  a2[1][0] = 1.0e+0;
  b2[0] = 5.0e-1; b2[1] = 5.0e-1;
  c2[1] = 1.0e+0;

  /* ブッチャー配列 (古典的ルンゲ・クッタ法) */
  a4[1][0] = 5.0e-1; a4[2][1] = 5.0e-1; a4[3][2] = 1.0e+0;
  b4[0] = 1.0/6; b4[1] = 1.0/3; b4[2] = 1.0/3; b4[3] = 1.0/6;
  c4[1] = 5.0e-1; c4[2] = 5.0e-1; c4[3] = 1.0e+0;

  /* 関数ポインタ */
  F = exF;

  fperr = fopen("exc-rk24-err.dat","w");
  printf("Max error:\n");
  printf("%-12s  %-22s  %-22s\n","h","Heun","Classical RK");

  h = 1.0;
  for(i = 1; i <= nitr; i++){
    h /= 10.0;
    ndiv = (int)(tlast / h);
    rval2 = explicit_rk(ndiv,h,u0,s2,a2,b2,c2,F,Urk2);
    rval4 = explicit_rk(ndiv,h,u0,s4,a4,b4,c4,F,Urk4);
    if (rval2 == 1){
      printf("ERROR: Heun method ...\n");
      rval = rval2;
      break;
    }
    else if(rval4 == 1){
      printf("ERROR: Classical Runge-Kutta method ...\n");
      rval = rval4;
      break;
    }

    /* 誤差の計算 */
    maxerrrk2 = fabs(trueu(0.0)-Urk2[0]);
    maxerrrk4 = fabs(trueu(0.0)-Urk4[0]);
    for(n = 1; n <= ndiv; n++){
      errrk2 = fabs(trueu(n*h) - Urk2[n]);
      if (errrk2 > maxerrrk2) maxerrrk2 = errrk2;
      errrk4 = fabs(trueu(n*h) - Urk4[n]);
      if (errrk4 > maxerrrk4) maxerrrk4 = errrk4;
    }
    printf("%e  %17.15e  %17.15e\n",h,maxerrrk2,maxerrrk4);
    /* fprintf*(fperr,"%17.15e  %17.15e  %17.15e\n",log10(1./h),log10(maxerrrk2),log10(maxerrrk4)); */
    fprintf(fperr,"%17.15e  %17.15e  %17.15e\n",1./h,maxerrrk2,maxerrrk4);
  }

  /* 計算結果の出力 */
  fp = fopen("exc-rk24-sol.dat","w");
  for(n = 0; n <= ndiv; n++) fprintf(fp,"%17.15e  %17.15e  %17.15e\n",n*h,Urk2[n],Urk4[n]);

  fclose(fp);
  fclose(fperr);

  free(Urk2);
  free(Urk4);
  free(b2);
  free(c2);
  free(b4);
  free(c4);
  for(i = 0; i < s2; i++) free(a2[i]);
  free(a2);
  for(i = 0; i < s4; i++) free(a4[i]);
  free(a4);

  return(rval);
}
