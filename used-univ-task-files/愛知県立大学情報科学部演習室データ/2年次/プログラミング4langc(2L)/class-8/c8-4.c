#include <stdio.h>
#define N (4)

int main(void) {

    float c[N], x, y;
    int i;
    for (i=N-1; i>=0; --i)
        scanf("%f", &c[i]);
    for (x=0.; x<=1.; x+=.1) {
        for (y=c[N-1], i=N-2; i>=0; --i)
            y = y * x + c[i];
        printf("%f %f\n", x, y);
    }
    return 0;
}