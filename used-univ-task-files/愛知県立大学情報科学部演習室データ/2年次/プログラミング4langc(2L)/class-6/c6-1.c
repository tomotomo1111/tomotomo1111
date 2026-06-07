#include <stdio.h>
#include <math.h>

#define True 1
#define False 0
int main(void) {
    float a, b, c, d;
    float x1, x2;
    int iNumFrag = False;

    scanf("%f %f %f", &a, &b, &c);

    d = b * b - 4 * a * c;
    iNumFrag = (d >= 0) ? False : True;
    d = fabs(d);
    x1 = (-b + sqrt(d) / (2 * a));
    x2 = (-b - sqrt(d) / (2 * a));
    if (iNumFrag) {
        printf("%f, %f\n", x1, x2);
    } else {
        printf("%f, %f\n", x1, x2);
    }
}