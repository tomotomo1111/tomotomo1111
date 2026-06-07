#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#define N 5

typedef double Vector[N];
const int n = N;
double dot(Vector vec) {
    int i;
    double d;

    d = 0.0;
    for (i = 0; i < n; ++i) d += vec[i] * vec[i];
    return d;
}

int main(void) {
    Vector vec0;
    int i;
    for (i = 0; i < n ;++i) {
        if (scanf("%lf", &vec0[i]) != 1) {
            printf("Read error at %d-th data\n", i + i);
            exit(1);
        }
    }

    printf("vector norm = %lf\n", sqrt(dot(vec0)));

    return 0;
}