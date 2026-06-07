#include <stdio.h>

int combination(int n, int r) {
    if (r == 0 || r == n) {
        return 1;
    } else {
        return combination(n-1, r) + combination(n-1, r-1);
    }
}

int main(void) {
    int n, r, ncr;

    scanf("%d %d", &n, &r);

    ncr = combination(n, r);

    printf("n=%d, r=%d, nCr=%ld\n",n ,r, ncr);

    return 0;
}