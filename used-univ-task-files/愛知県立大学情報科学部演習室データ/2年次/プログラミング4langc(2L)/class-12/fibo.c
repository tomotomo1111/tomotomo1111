#include <stdio.h>

int fibonacci(int n) {
    /*
    if (n == 1 | n == 2) {
        return 1;
    } else {
        return fibonacci(n-1) + fibonacci(n-2);
    }
    */
    int temp = 1;
    int newtemp = 2;
    int oldtemp = 1;
    int k = 0;
    if (n == 1 || n == 2) return 1;
    do {
        newtemp = temp + oldtemp;
        oldtemp = temp;
        temp = newtemp;
        k++;
    } while (k < n - 2);
    return newtemp;
}

int main(void) {
    int i, n;

    scanf("%d", &n);

    for (i=1; i<=n; ++i) printf("%ld ", fibonacci(i));
    printf("\n");

    return 0;
}