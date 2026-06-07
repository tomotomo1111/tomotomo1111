#include <stdio.h>

int main(void) {
    int y;
    char g;
    scanf("%c %d", &g, &y);
    switch (g) {
        case 'm':
        case 'M':
            printf("%d\n", y + 1889);
            break;
        case 't':
        case 'T':
            printf("%d\n", y + 1911);
            break;
        case 's':
        case 'S':
            printf("%d\n", y + 1925);
            break;
        case 'h':
        case 'H':
            printf("%d\n", y + 1988);
            break;
        case 'r':
        case 'R':
            printf("%d\n", y + 2018);
            break;
        default:
            printf("?\n");
    }

    return 0;
}