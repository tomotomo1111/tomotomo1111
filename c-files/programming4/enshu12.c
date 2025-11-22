/*

    [入力]
    N?10

    [ランダム値]
    0 30
    1 96
    2 15
    3 18
    4 44
    5 83
    6 66
    7 70
    8 88
    9 34
    
    [出力]
    15 18 30 34 44 66 70 83 88 96

*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void quicksort(int array[], int l, int r) {
    if (l < r) {
        int pivot = array[(r + l) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (array[i] < pivot) i++;
            while (array[j] > pivot) j--;
            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        quicksort(array, l, j);
        quicksort(array, i, r);
    }
}

int main(void) {

    int i, n, p[1024];

    srand((unsigned)time(NULL));
    printf("N?");
    scanf("%d", &n);

    for (i = 0; i < n; i++) {
        p[i] = rand() % (100+1);
        printf("%d %d\n", i, p[i]);
    }

    quicksort(p, 0, n - 1);

    putchar('\n');
    for (i = 0; i < n; i++) {
        printf("%d ", p[i]);
    }
    return 0;
}