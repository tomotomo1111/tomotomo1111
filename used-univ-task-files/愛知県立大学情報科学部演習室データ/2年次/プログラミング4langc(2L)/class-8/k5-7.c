#include <stdio.h>

#define NUMBER 120

int main(void) {

    int num;
    int tensu[NUMBER];
    int bunpu[11] = {0};

    printf("人数を入力せよ:");

    do {
        scanf("%d", &num);
        if (num < 1 || num > NUMBER)
            printf("\a1〜%dで入力せよ:", NUMBER);
    } while (num < 1 || num > NUMBER);

    printf("%d人の点数を入力せよ。\n", num);

    for (int i = 0; i < num; i++) {
        printf("%2d番:", i + 1);
        do {
            scanf("%d", &tensu[i]);
            if (tensu[i] < 0 || tensu[i] > 100)
                printf("\a0〜100で入力せよ:");
        } while (tensu[i] < 0 || tensu[i] > 100);
        bunpu[tensu[i] / 10]++;
    }

    puts("\n---分布グラフ---");

    for (int i = 0; i < 10; i++) {
        printf("%3d〜%3d:", i * 10, i * 10 + 9);
        for (int j = 0; j < bunpu[i]; j++)
            putchar('*');
        putchar('\n');
    }

    printf("   100:");
    for (int j = 0; j < bunpu[10]; j++)
        putchar('*');
    putchar('\n');

    return 0;
}