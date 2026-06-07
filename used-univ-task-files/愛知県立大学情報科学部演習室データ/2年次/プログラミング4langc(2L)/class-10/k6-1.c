#include <stdio.h>

int min2(int a, int b) {
    return (a < b) ? a : b;
}
int min4(int a, int b, int c, int d) {
    return min2(min2(a, b), min2(c, d));
}

int main(void) {
    int n1, n2, n3, n4;
    puts("四つの整数を入力してください。");
    printf("整数n1："); scanf("%d", &n1);
    printf("整数n2："); scanf("%d", &n2);
    printf("整数n3："); scanf("%d", &n3);
    printf("整数n4："); scanf("%d", &n4);
    printf("最も小さい値は%dです。¥n", min4(n1, n2, n3, n4));
    return 0;
}