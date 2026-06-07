#include <stdio.h>

int main(void) {
    int a, b;

    puts("2つの整数を入力しせよ.");
    printf("整数 a : "); scanf("%d", &a);
    printf("整数 b : "); scanf("%d", &b);

    printf("aの値はbの値の%f%%です.\n",((double) a / b) * 100;
    return 0;
}