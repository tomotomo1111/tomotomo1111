#include <stdio.h>
int main(void) {
    float a[4][3], b[3][4];
    float temp[4][4];
    int i, j;

    for (i=0; i<4; ++i) {
        for (j=0; j<4; ++j) {
            temp[i][j] = 0;
        }
    }
    for (i=0; i<4; ++i)
        for (j=0; j<3; ++j)
            scanf("%f", &a[i][j]);
    for (i=0; i<3; ++i)
        for (j=0; j<4; ++j)
            scanf("%f", &b[i][j]);
    for (i=0; i<4; ++i) {
        for (j=0; j<4; ++j) {
            for (int k = 0; k < 3; ++k) {
                temp[i][j] += a[i][k] * b[k][j];
            }
        }
    }
    for (i=0; i<4; ++i)
        printf("%6.2f %6.2f %6.2f \n", a[i][0], a[i][1], a[i][2]);
    for (i=0; i<3; ++i)
        printf("%6.2f %6.2f %6.2f %6.2f \n", b[i][0], b[i][1], b[i][2], b[i][3]);
    for (i=0; i<4; ++i)
        printf("%6.2f %6.2f %6.2f %6.2f \n", temp[i][0], temp[i][1], temp[i][2]);
    return 0;
}