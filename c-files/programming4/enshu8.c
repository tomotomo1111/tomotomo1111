/*
    [入力]
        次元 :
        4
        4次正方行列 A の係数を入力
        a00 : 2
        a01 : 3
        a02 : 4
        a03 : 5
        a10 : 1
        a11 : 0
        a12 : 0
        a13 : 1
        a20 : 2
        a21 : 5
        a22 : 1
        a23 : 0
        a30 : 0
        a31 : 0
        a32 : 0
        a33 : 1
        4次列ベクトル B の係数を入力
        b0 : 45
        b1 : 56
        b2 : 48
        b3 : 4
    [出力]
        |  2.00   3.00   4.00   5.00 || x0 | | 45.00 |
        |  1.00   0.00   0.00   1.00 || x1 | | 56.00 |
        |  2.00   5.00   1.00   0.00 || x2 |=| 48.00 |
        |  0.00   0.00   0.00   1.00 || x3 | |  4.00 |
        x0 =      52.00
        x1 =      -8.53
        x2 =     -13.35
        x3 =       4.00
*/


#include <stdio.h>
#include <math.h>

int main(void) {
    int N;
    float A[N][N];
    float B[N];
    
    puts("次元 : ");
    scanf("%d", &N);

    printf("%d次正方行列 A の係数を入力\n", N);
    for (int j = 0; j < N; j++) {
        for (int i = 0; i < N; i++) {
            printf("a%d%d : ", j, i);
            scanf("%f", &A[j][i]);
        }
    }
    printf("%d次列ベクトル B の係数を入力\n", N);
    for (int j = 0; j < N; j++) {
        printf("b%d : ", j);
        scanf("%f", &B[j]);
    }

    for (int j = 0; j < N; j++) {
        putchar('|');
        for (int i = 0; i < N; i++) printf(" %5.2f ", A[j][i]);
        printf("|| x%d |", j);
        printf("%c", (j == N / 2) ? '=' : ' ');
        printf("| %5.2f |\n", B[j]);
    }

    int k, ip;
    float amax, temp;

    for (k = 0 ; k < N - 1 ; k++) {
        amax = abs(A[k][k]);
        ip = k;
        for (int i = k + 1 ; i < N ; i++) {
            if (abs(A[i][k]) > amax) {
                amax = abs(A[i][k]); 
                ip = i;
            }
        }
    
        if (ip != k) {
            for (int j = k ; j < N ; j++) {
                temp = A[k][j];
                A[k][j] = A[ip][j];
                A[ip][j] = temp;
            }
            temp = B[k];
            B[k] = B[ip];
            B[ip] = temp;
        }

        for (int i = k + 1; i < N; i++) {
            A[i][k] /= A[k][k];
            for (int j = k + 1; j < N; j++) A[i][j] -= A[i][k] * A[k][j];
            B[i] -= A[i][k] * B[k];
        }
    }

    B[N - 1] /= A[N - 1][N - 1];
    for (k = N - 2; k >= 0; k--) {
        for (int j = k + 1; j < N; j++) B[k] -= A[k][j] * B[j];
        B[k] /= A[k][k];
    }

    for (int i = 0; i < N; i++) {
        printf("x%d = %10.2f\n", i, B[i]);
    }

    return 0;
}