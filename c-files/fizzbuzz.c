#include <stdio.h>

int main(void) {
    int n, i, j;
    puts("Put a Number : ");
    scanf("%d", &n);
    puts("-----------------");
    int three, five;
    for (j = 1; j <= n; j++) {
        three = j % 3; five = j % 5;
        if (three == 0) printf("%s", "fizz");
        if (five == 0) printf("%s", "buzz");
        if (three != 0 && five != 0) printf("%d", j);
        putchar('\n');
    }
}