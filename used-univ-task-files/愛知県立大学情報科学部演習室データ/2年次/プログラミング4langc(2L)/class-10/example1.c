#include <stdio.h>
#define NUMBER 5

extern int top(void);
int tensu[NUMBER];

int main(void) {
    extern int tensu[];
    printf("%d人の点数を入力せよ。\n", NUMBER);
    for (int i = 0; i < NUMBER; i++) {
        printf("%d : ", i + 1);
        scanf("%d", &tensu[i]);
    }
    printf("最高点=%d\n", top());
    return 0;
}