#include <stdio.h>

int main(void) {

    int no;
    int ini = 0;

    printf("整数を入力してください");
    scanf("%d", &no);

    switch (no) {
        case -1:
            puts("その数は負です.");
            break;
        case 0:
            puts("その数は 0 です.");
            break;
        case 1:
            puts("その数は正です.");
            break;
        default:
            puts("異常終了");
            return -1;
    }

    return 0;
}