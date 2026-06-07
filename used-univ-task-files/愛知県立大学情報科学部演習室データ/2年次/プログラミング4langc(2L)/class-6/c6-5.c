#include <stdio.h>

int main(void) {
    int no;

    printf("整数を入力してください.");
    scanf("%d", &no);

    switch(no % 3) {
        case 0: puts("その数は 3 で割り切れます."); break;
        case 1: puts("その数は 3 で割った剰余は 1 です."); break;
        case 2: puts("その数は 3 で割った剰余は 2 です."); break;
    }
}