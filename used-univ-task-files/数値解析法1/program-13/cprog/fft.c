/*
FFTプログラム
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/*
  FFT計算プログラム
  [入力]
    m = サンプリング数の指数（n = 2^m）
    f: 1次元配列 (n次元, サンプリング値ベクトル)
  [出力]
    c: 2次元配列 (n次元, FFT行列)
 */
void fft(int m, double *f, double **c)
{
  int j, p, pl, q, l, lq, k;
  int rev;
  int n = round(pow(2,m));
  double cr, ci;

  /* 回転因子 */
  double theta, wq_r, wq_i;

  /* サンプリング値の格納 */
  for (j = 0; j < n; j++) c[j][0] = f[j];

  /* [演習] FFT関数を完成させてください */

  /* ビットリバースによる並び替え */
  int i = 0;
  double tmpFFT_r, tmpFFT_i;
  for (j = 1; j < n - 1; j++) {
    for (k = n >> 1; k > (i ^= k); k >>= 1);
    if (j < i) {
      tmpFFT_r = c[j][0];
      tmpFFT_i = c[j][1];
	c[j][0] = c[i][0];
	c[j][1] = c[i][1];
	c[i][0] = tmpFFT_r;
	c[i][1] = tmpFFT_i;
    }
  }
 }

/*
  IFFT計算プログラム
  [入力]
    m = サンプリング数の指数（n = 2^m）
    c: 2次元配列 (n次元, FFT行列)
  [出力]
    f: 2次元配列 (n次元, サンプリング値行列)
*/
void ifft(int m, double **c, double **f){
  int j, p, pl, q, l, lq, k;
  int rev, n = round(pow(2,m));
  double fr, fi;

  /* 回転因子 */
  double theta, wq_r, wq_i;

  /* [演習] IFFT関数を完成させてください */

  /* ビットリバースによる並び替え */
  int i = 0;
  double tmpIFFT_r, tmpIFFT_i;
  for (j = 1; j < n - 1; j++) {
    for (k = n >> 1; k > (i ^= k); k >>= 1);
    if (j < i) {
      tmpIFFT_r = f[j][0];
      tmpIFFT_i = f[j][1];
	f[j][0] = f[i][0];
	f[j][1] = f[i][1];
	f[i][0] = tmpIFFT_r;
	f[i][1] = tmpIFFT_i;
    }
  }
 }
