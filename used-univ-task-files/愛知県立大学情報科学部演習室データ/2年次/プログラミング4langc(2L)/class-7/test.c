#include <stdio.h>

int main(void) {
    int A[4][3] = {{1, 2, -2}, {-1, 7, 6}, {4, 5, 3}, {-3, -9, 8}};
    char s1[] = "NAGOYA_Grampus_Eight";
    char s2[] = "FC_GIFU";
    char s3[] = "Veertien_MIE";

    printf("%s\n%s\n%s\n", s1, s2, s3);
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 3; j++) {
            printf("%d ",A[i][j]);
        }
        putchar('\n');
    }
    return 1;
}