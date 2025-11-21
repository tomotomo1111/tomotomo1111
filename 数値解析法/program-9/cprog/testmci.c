/*
このプログラムを使用する場合は，まずmci.cを完成させてください．
完成後，コンパイルしてください．

[UbuntuなどLinux, OS X (gccインストール済), Windows (MSYS2+MinGW) の場合]
gcc -o testmci testmci.c mci.c -lm
注) 演習室WSのCentOSでも同じ

コンパイルに成功した場合は，./testmci で実行できます．
計算結果を確認してください．

[実行結果] ※まったく同じ結果にはならないはずです．
   M          MCI         Abs error
       2  9.868126e-02  2.243931e-03
       4  1.108705e-01  9.945271e-03
       8  7.953312e-02  2.139207e-02
      16  8.822769e-02  1.269750e-02
      32  9.637892e-02  4.546274e-03
      64  9.383178e-02  7.093412e-03
     128  1.027719e-01  1.846732e-03
     256  1.013429e-01  4.176957e-04
     512  9.904731e-02  1.877878e-03
    1024  9.822587e-02  2.699319e-03
    2048  9.888424e-02  2.040952e-03
    4096  9.973770e-02  1.187489e-03
    8192  1.009468e-01  2.159020e-05
   16384  1.017622e-01  8.370532e-04
   32768  1.016842e-01  7.590120e-04
   65536  1.013417e-01  4.165507e-04
  131072  1.010824e-01  1.572038e-04
  262144  1.008435e-01  8.169828e-05
  524288  1.009779e-01  5.272485e-05
 1048576  1.009217e-01  3.507840e-06
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

double mcint(int n, double *a, double *b, int m, double (* f)(int,double *));

/* 確認用関数 */
double testf(int n, double *x)
{
  int i;
  double xval = 0.0;

  for(i = 0; i < n; i++) xval += x[i];
  /* 例題 */
  return(exp(-xval));
}

/*
 演習問題確認プログラム (モンテカルロ法)
*/
int main(void)
{
  int k;
  double mcval,mcerr;
  double (* func)(int,double*);
  FILE *fp;

  /* 空間次元 */
  int n = 5;
  /* 矩形領域区間 */
  double a[] = {0.0,0.0,0.0,0.0,0.0};
  double b[] = {1.0,1.0,1.0,1.0,1.0};

  /* 真の値 */
  double eval = pow(1.0-exp(-1.0),5);

  /* 分点数（初期値） */
  int m = 1;
  
  /* 最大分点数 m * 2^mmax */
  int mmax = 20;

  /* 関数ポインタ */
  func = testf;

  /* 数値積分値の計算 */
  fp = fopen("exc-mci-err.dat","w");
  printf("   M          MCI         Abs error\n");
  for(k = 1; k <= mmax; k++)
  {
    m *= 2;
    mcval = mcint(n,a,b,m,func);
    mcerr = fabs(mcval-eval);

    printf("%8d  %e  %e\n",m,mcval,mcerr);
    fprintf(fp,"%d  %17.15e\n",m,mcerr);
  }
  
  fclose(fp);
  
  return(0);
}
