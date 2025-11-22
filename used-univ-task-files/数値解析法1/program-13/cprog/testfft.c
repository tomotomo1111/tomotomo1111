/*
このプログラムを使用する場合は，まずfft.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testfft testfft.c fft.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testfft で実行できます．
計算結果を確認してください．

[実行例]
Calculation time = 6.653000000000000e-04 [s]
Abs. error (IFFT) = 7.771561172376096e-16
注) 実行時間は計算環境により異なります．
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

void fft(int n, double *f, double **c);
void ifft(int m, double **c, double **f);

/* 例題関数 */
double testf(double t){
  double fval, freq = 40.0;

  if (t >= 0.0 && t <= 4.0){
    fval = sin(2*M_PI*freq*t);
  }
  else{
    fval = 0.0;
  }

  return(fval);
}

/*
 演習問題確認プログラム (FFT)
*/
int main(void){
  int j;
  double ifft_err = 0.0, abserr;
  double *f, **c, **ifft_f;

  /* 計算時間確認用変数 */
  struct timespec start,end;

  /* サンプリング数 */
  int k = 12;
  int n = round(pow(2,k));

  /* サンプリング間隔 */
  double T = 4.0;
  double dt = T / n;

  /* 配列の作成 */
  f = (double *)calloc(n,sizeof(double));
  c = (double **)calloc(n,sizeof(double *));
  ifft_f = (double **)calloc(n,sizeof(double *));
  if(f == NULL || c == NULL || ifft_f == NULL){
    printf("領域の確保に失敗\n");
    return(1);
  }
  for(j = 0; j < n; j++){
    c[j] = (double *)calloc(n,sizeof(double));
    ifft_f[j] = (double *)calloc(n,sizeof(double));
    if(c[j] == NULL || ifft_f[j] == NULL){
      printf("領域の確保に失敗\n");
      exit(1);
    }
  }

  /* サンプリング値の格納 */
  for(j = 0; j < n; j++){
    f[j] = testf(j * dt);
  }

  /* FFTの計算 */
  clock_gettime(CLOCK_REALTIME, &start);
  fft(k, f, c);
  clock_gettime(CLOCK_REALTIME, &end);
  double time_spent = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1000000000.0;
  printf("Calculation time = %17.15e [s]\n",time_spent);

  /* IFFTの計算 */
  ifft(k, c, ifft_f);

  /* 結果の出力 */
  FILE *fp;
  fp = fopen("exc-fft.dat","w");
  for (j = 0; j < n; j++){
    fprintf(fp,"%17.15e  %17.15e  %17.15e  %17.15e\n",j/T,c[j][0],c[j][1],(c[j][0]*c[j][0]+c[j][1]*c[j][1])/n);
    abserr = fabs(ifft_f[j][0] - f[j]);
    if (ifft_err < abserr) ifft_err = abserr;
  }

  fclose(fp);
  printf("Abs. error (IFFT) = %17.15e\n",ifft_err);

  free(f);
  for(j = 0; j < n ; j++){
    free(c[j]);
    free(ifft_f[j]);
 }
 free(c);
 free(ifft_f);

 return(0);
}
