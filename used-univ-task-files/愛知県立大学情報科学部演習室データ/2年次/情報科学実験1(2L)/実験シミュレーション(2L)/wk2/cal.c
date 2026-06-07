#include <stdio.h>
#include <stdlib.h>

int main(void) {
    double a = 0.;
    double n;
    double M, m, mc, p, kc, ksp;
    M = 32768.;
    m = 2754.;
    mc = 1978.;
    p = 941.375;
    kc = 1.877;
    ksp = 1.821;
    n = 8.;
    
    a = (M - m - mc - n * p) / (1 + kc + ksp);
    printf("n = %f: %5.2f", n, a);
    return 1; 
}