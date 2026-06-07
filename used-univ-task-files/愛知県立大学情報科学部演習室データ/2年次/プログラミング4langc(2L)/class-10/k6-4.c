#include <stdio.h>

int sqr(int x) {
    return x * x;
}

int pow4(int x) {
    return sqr(sqr(x));
}

int main(void) {
    int n1;
    puts("四つの整数を入力してください。");
    printf("整数n1："); scanf("%d", &n1);
    printf("n1の四乗の値は%dです。¥n", pow4(n1));
    return 0;
}