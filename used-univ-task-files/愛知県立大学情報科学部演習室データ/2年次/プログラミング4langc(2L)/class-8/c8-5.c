#include <stdio.h>

int main(void) {
    char s[11], t;

    scanf("%10s", s);

    s[1] = s[0];
    s[0] = 'X';

    t = s[2];
    s[2] = s[3];
    s[3] = t;

    printf("%s\n", s);
    
    return 0;
}