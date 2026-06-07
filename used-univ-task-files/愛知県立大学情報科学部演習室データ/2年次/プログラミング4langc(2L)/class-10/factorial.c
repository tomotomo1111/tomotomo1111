#include <stdio.h>
int factorial(int x) {
    int i, f;
    i = 0;
    f = 1;
    while (i<x) {
        ++i;
        f = f * i;
    }
    printf("factorial called, %3d!=%5.2ld\n", x, f);
    return f;
}