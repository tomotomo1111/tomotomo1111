#include <stdio.h>

int main(void) {
    int i, n;
    float fact;

    scanf("%d", &n);

    fact = 1;

    for (i = 2; i <=n; ++i) {
        fact = fact * i;
        printf("%d %.0f\n", i, fact);
    }

    printf("%d! = %.0f\n", n, fact);

    return 0;
}