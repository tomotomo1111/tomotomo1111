#include <stdio.h>

int main(void) {
    int n = 1;
    int m = 1;

    printf("before increment\n");
    printf("n = %d, m = %d\n",++n,m++);
    printf("after increment\n");
    printf("n = %d, m = %d\n",n,m);
    return 0;
}