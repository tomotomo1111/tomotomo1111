#include <stdio.h>
#include <math.h>
#define kizami (6)
#define pi (3.1415)

int main(void) {
    float x, sinx, cosx;
    int i, kakudo;
    printf("do : sin(x), cos(x)\n");
    for(i = 0; i <= kizami; ++i) {
        kakudo = (180. / kizami) * i;
        printf("%3d:", kakudo);
        x = kakudo / 180. * pi;
        sinx = sin(x);
        cosx = cos(x);
        printf("%7.4f, %7.4f\n", sinx, cosx);
    }

    return 0;
}