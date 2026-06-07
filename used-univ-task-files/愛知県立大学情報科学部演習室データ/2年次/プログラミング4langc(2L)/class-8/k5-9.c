#include <stdio.h>

int main(void) {

    int a[5];
    int b[5];

    for (int i = 0; i < 5; i++) {
        printf("a[%d] : ", i);
        scanf("%d", &a[i]);
    }

    for (int i = 0; i < 5; i++)
        b[4 - i] = a[i];

    puts("  a   b");
    puts("---------");
    for (int i = 0; i < 5; i++)
        printf("%4d%4d\n", a[i], b[i]);
    
    return 0;
}