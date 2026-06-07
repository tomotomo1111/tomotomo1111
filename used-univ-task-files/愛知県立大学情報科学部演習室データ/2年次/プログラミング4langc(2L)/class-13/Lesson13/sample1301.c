/* 2次関数の計算 */
#include <stdio.h>
#include <stdlib.h>

#define square(x)      ((x) * (x))
#define NMAX 100
#define DEFAULTMAX 5

int func_a(int x)
{
    int y;

    y = square(x) + x + 1;

    return y;
}

int main(void)
{
    int x, y[NMAX], maxnum;

    puts("maxnum?");
    if (scanf("%d", &maxnum) != 1)
        maxnum = DEFAULTMAX;

    for (x = 0; x <= maxnum; x++)
        y[x] = func_a(x);

    for (x = 0; x <= maxnum; x++)
        printf("x=%d, y=%d\n", x, y[x]);

    return 0;
}
