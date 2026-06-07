#include <stdio.h>

void cal(int a, int b, int c, int d, int *e, int *f);

int main() {
    int a, b, c, d, e, f;
    scanf("%d %d %d %d", &a, &b, &c, &d);
    if (b == 0 || d == 0) {
        puts("divide by 0");
        return 0;
    }
    cal(a, b, c, d, &e, &f);
    if (f == 1) {
        printf("%d/%d%+d/%d=%+d", a, b, c, d, e);
    } else {
        printf("%d/%d%+d/%d=%+d/%d", a, b, c, d, e, f);
    }
    return 1;
}
void cal(int a, int b, int c, int d, int *e, int *f) {
    int ad, cb, bd, adcb, p, q, r;
    ad = a * d;
    cb = c * b;
    bd = b * d;
    adcb = ad + cb;
    if (adcb >= bd) {
        p = adcb;
        q = bd;
    }
    if (adcb < bd) {
        p = bd;
        q = adcb;
    }
    while (r >= 0) {
        r = p % q;
        if (r == 0) break;
        p = q;
        q = r;
    }
    *e = adcb / q;
    *f = bd / q;
}