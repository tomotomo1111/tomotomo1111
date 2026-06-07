#include <stdio.h>

int main(void) {
    int n;

    printf("nの値 : ");
    scanf("%d",&n);

    int sum = 0;

    for (int i = 0; i <= n; i++) sum += i;
    printf("1から%dまでの総和は%dです。",n ,sum);
}