#include <stdio.h>
void cal(int a, int b, int c, int d, int *e, int *f);

int main() {
    int a, b, c, d, e, f;

    scanf("%d %d %d %d", &a, &b, &c, &d);
    if (b == 0 || d == 0) {
        puts("Divide by Zero");
        return 0;
    }
    cal(a, b, c, d, &e, &f);
    if (f != 1) {
        printf("%d/%d%+d/%d=%d/%d", a, b, c, d, e, f);
    } else {
        printf("%d/%d%+d/%d=%d", a, b, c, d, e);
    }

    return 1;
}

void cal(int a, int b, int c, int d, int *e, int *f) {

    int bd, adbc, p, q, r;
    bd = b * d;
    adbc = a * d + b * c;

    p = adbc;
    q = bd;
    r = 1;
    while (r != 0) {
        r = p % q;
        p = q;
        q = r;
    }

    *e = adbc / p;
    *f = bd / p;
}