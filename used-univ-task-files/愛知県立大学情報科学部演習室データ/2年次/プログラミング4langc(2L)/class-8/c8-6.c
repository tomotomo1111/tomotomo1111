#include <stdio.h>
int main(void) {
    float a[3][3], b[3][3], c[3][3];
    int i, j;
    for (i=0; i<3; ++i)
        for (j=0; j<3; ++j)
            scanf("%f", &a[i][j]);
    for (i=0; i<3; ++i)
        for (j=0; j<3; ++j)
            scanf("%f", &b[i][j]);
    for (i=0; i<3; ++i)
        for (j=0; j<3; ++j)
            c[i][j] = a[i][j] + b[i][j];
    for (i=0; i<3; ++i) {
        printf("%6.2f %6.2f %6.2f ", a[i][0], a[i][1], a[i][2]);
        printf("%6.2f %6.2f %6.2f ", b[i][0], b[i][1], b[i][2]);
        printf("%6.2f %6.2f %6.2f\n", c[i][0], c[i][1], c[i][2]);
    }
    return 0;
}