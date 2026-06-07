#include <stdio.h>

extern int factorial(int x);

int main(void) {
    int n, r, ncr;
    scanf("%d %d", &n, &r);
    ncr = factorial(n) / (factorial(r) * factorial(n-r));
    printf("n=%d, r=%d, nCr=%ld\n", n, r, ncr);
    return 0;
}