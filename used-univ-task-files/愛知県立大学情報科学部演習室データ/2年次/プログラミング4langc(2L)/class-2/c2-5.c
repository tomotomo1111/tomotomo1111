#include <stdio.h>

int main(void) {
    int a, b;

    puts("2つの整数を入力してください.");
    printf("整数 a : "); scanf("%d", &a);
    printf("整数 b : "); scanf("%d", &b);
    
    printf("それらの平均は%1.0fです.\n",(double)(a + b) / 2);
    return 0;
}