#include <stdio.h>
#include <stdlib.h>

void tashizan(double **, double **, double **, int, int);

int main(void){
    int i, j;
    int tate = 3, yoko = 3;
    double matrix1[3][3], matrix2[3][3], result[3][3];

    for( i = 0; i < tate; i++ ){
       scanf( "%lf %lf %lf", &matrix1[i][0], &matrix1[i][1], &matrix1[i][2] );
    }    
    for( i = 0; i < tate; i++ ){
       scanf( "%lf %lf %lf", &matrix2[i][0], &matrix2[i][1], &matrix2[i][2] );
    }

    tashizan(matrix1, matrix2, result, tate, yoko);

    for( i = 0; i < tate; i++ ){
        for( j = 0; j < yoko; j++ ){
           printf( "%f ", result[i][j] );
        }
        printf( "\n" );
    }
    return 0;
}
void tashizan(double **a, double **b, double **r, int tate, int yoko){
    for (int i = 0; i < tate; i++) {
        for (int j = 0; j < yoko; j++) {
            r[i][j] = a[i][j] + b[i][j];
        }
    }
}