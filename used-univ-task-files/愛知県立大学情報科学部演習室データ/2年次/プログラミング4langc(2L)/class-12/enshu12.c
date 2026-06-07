#include <stdio.h>
#include <time.h>

int sort(int *array[]) {
    
}

int main(void) {

    int i, n, p[1024];

    srand((unsigned)time(NULL));
    printf("N?");
    scanf("%d", &n)

    for (i=0; i<n; i++) {
        p[i] = rand() % 100+1;
        printf("%d %d\n", i, p[i]);
    }

    sort(p);
    return 0;
}