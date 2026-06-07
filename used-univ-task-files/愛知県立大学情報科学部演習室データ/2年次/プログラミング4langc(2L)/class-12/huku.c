#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void xy_rt(double x, double y, double *pr, double *pt) {
    *pr = sqrt(x * x + y * y);
    *pt = atan(y / x);
    
    printf("xy_rt called,x,y,r,t|=%6.2lf,%6.2lf,%6.2lf,%6.2lf\n", x, y, *pr, *pt);
}

int main(void) {
    double x1, y1, x2, y2, r1, t1, r2, t2;

    if (scanf("%lf %lf %lf %lf", &x1, &y1, &x2, &y2) != 4) {
        printf("Input 4 numbers\n");
        exit(-1);
    }

    xy_rt(x1, y1, &r1, &t1);
    xy_rt(x2, y2, &r2, &t2);

    printf("answer=%6.2lf, %6.2lf\n", (r1 * r2) * cos(t1 + t2), (r1 * r2) * sin(t1 + t2));

    return 0;
}