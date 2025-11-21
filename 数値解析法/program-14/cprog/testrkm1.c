/*
このプログラムを使用する場合は，まずrkm.c内のeuler関数を完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testrkm1 testrkm1.c rkm.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testrkm1 で実行できます．
計算結果を確認してください．

[実行結果]
Euler method:
       h          Max error
1.000000e-01  2.095333741540450e-02
1.000000e-02  2.029755482791717e-03
1.000000e-03  2.023126000113562e-04
1.000000e-04  2.022462467576869e-05
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

void euler(int ndiv, double h, double u0, double (* F)(double,double),double *U);

/* 真の解 (8ページ) */
double trueu(double t){
  return(exp(-t*t/2));
}

/* 例題関数 (8ページ) */
double exF(double t, double u){
  return(-t * u);
}

/*
 演習問題確認プログラム (オイラー法)
*/
int main(void){
  int i,n,ndiv;
  double h;
  double (* F)(double,double);

  /* 例題用設定 */
  char fname[20],str[3];
  double hmin = 1.0e-4, tlast = 5.0e-1;
  int nmax = (int)(tlast / hmin), nitr = 4;
  double u0 = 1.0,t,abserr,maxerr;
  double *U;
  FILE *fp,*fperr;

  U = (double *)calloc(nmax + 1,sizeof(double));
  if(U == NULL){
    printf("領域の確保に失敗\n");
    return(1);
  }
  
  /* 関数ポインタ (8ページ例題) */
  F = exF;

  fperr = fopen("exc-rk1-err.dat","w");

  printf("Euler method:\n");
  printf("%8s  %17s\n","h","Max error");
  h = 1.0;
  for(i = 1; i <= nitr; i++){
    h /= 10.0;
    ndiv = (int)(tlast / h);
    euler(ndiv,h,u0,F,U);

    /* 計算結果の出力 */
    snprintf(str,sizeof(str),"%d",i);
    sprintf(fname,"exc-rk1-sol-%s.dat",str);
    fp = fopen(fname,"w");
    for(n = 0; n <= ndiv; n++) fprintf(fp,"%17.15e  %17.15e\n",n*h,U[n]);
    fclose(fp);

    /* 誤差の計算 */
    maxerr = fabs(trueu(0.0)-U[0]);
    for(n = 1; n <= ndiv; n++){
      abserr = fabs(trueu(n*h) - U[n]);
      if (abserr > maxerr) maxerr = abserr;
    }
    printf("%e  %17.15e\n",h,maxerr);
    /* fprintf(fperr,"%17.15e  %17.15e\n",log10(1./h),log10(abserr)); */
    fprintf(fperr,"%17.15e  %17.15e\n",1./h,abserr);
  }

  fclose(fperr);

  free(U);
      
  return(0);
}
