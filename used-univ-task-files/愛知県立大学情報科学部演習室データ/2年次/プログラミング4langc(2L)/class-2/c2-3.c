#include <stdio.h>

int main(void) {
    int n1, n2;

    puts("2つの整数を入力してください.");
    printf("整数1:"); scanf("%d", &n1);
    printf("整数2:"); scanf("%d", &n2);

    int wa = n1 + n2;

    printf("それらの和は%dです.\n", wa);
    printf("すなわちn1 + n2 = %dです.\n", wa);
    return 0;
}