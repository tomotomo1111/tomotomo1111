#include <stdio.h>

int main(void) {
    int no1, no2;

    puts("2つの整数を入力せよ。");
    printf("整数a : ");
    scanf("%d",&no1);
    printf("整数a : ");
    scanf("%d",&no2);

    int sum = 0;

    for (int i = no1; i <= no2; i++) sum += i;
    printf("%d以上%d以下の全整数の和は%dです。",no1 ,no2 ,sum);
}