#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

#define N 1000
#define MEMOSIZE 6
#define BOSS 8
#define LOT 6
#define INTERVAL 700
#define DIV_MAX 5

extern int newBoss(void);
extern void dhondtstyle(int *, int *);

int main(void) {
    srand((unsigned int) time(NULL));
    for (int i = 0; i < N; i++) {
        newBoss();
        Sleep(INTERVAL);
    }
    return 1;
}

int newBoss(void) {
    static int memo[MEMOSIZE];
    static int initoffset = MEMOSIZE;
    int dhondt_alloc_a[BOSS];
    int dhondt_alloc_b[LOT];
    int b, j, t1, t2;
    
    if (initoffset < 0) {
        dhondtstyle(memo, dhondt_alloc_a);
        t2 = 0;
        for (j = 0; j < BOSS; j++) {
            while (dhondt_alloc_a[j] > 0) {
                dhondt_alloc_b[t2++] = j;
                dhondt_alloc_a[j]--;
            }
        }
        printf("\n[lottery]\n");
        for (j = 0; j < LOT; j++) printf("%d ",dhondt_alloc_b[j]);
        b = (rand() % LOT);
        t1 = dhondt_alloc_b[b];
    } else { 
        b = (rand() % BOSS);
        t1 = b;
    }
    for (j = 0; j < MEMOSIZE - 1; j++) memo[j] = memo[j+1];
    memo[MEMOSIZE - 1] = t1;

    printf("\nnew -> %d", t1);
    printf("\npast %d history: ", MEMOSIZE);
    for (j = 0; j < MEMOSIZE; j++) printf("%d ", memo[j]);
    putchar('\n');

    initoffset--;
    return b;
}

void dhondtstyle(int *a, int *b) {
    int count[BOSS] = {0};
    int bmcount[BOSS];
    int name[BOSS];
    int dhondt_arr[DIV_MAX][BOSS];
    int alloc_arr[BOSS] = {0};

    int i, j, k, t1, t2, t3, t4, t5;
    int max;
    int allocation;

    for (i = 0; i < BOSS; i++) {
        for (j = 0; j < MEMOSIZE; j++) if (a[j] == i) count[i]++;
        name[i] = i;
    }

    max = count[0];
    for (j = 1; j < BOSS; j++) if (count[j] > max) max = count[j];
    for (j = 0; j < BOSS; j++) bmcount[j] = max + 1 - count[j];

    printf("%% before sort %%\n");
    printf("name        : ");
    for (j = 0; j < BOSS; j++) printf("%d ", name[j]);
    printf("\ncount       : ");
    for (j = 0; j < BOSS; j++) printf("%d ", count[j]);
    printf("\nmax+1-count : ");
    for (j = 0; j < BOSS; j++) printf("%d ", bmcount[j]);
    
    for (i = 0; i < BOSS; i++) {
        for (j = 0; j < BOSS - i - 1; j++) {
            if (bmcount[j] > bmcount[j + 1]) {
                t1 = name[j + 1]; t2 = count[j + 1]; t3 = bmcount[j + 1]; 
                name[j + 1] = name[j]; count[j + 1] = count[j]; bmcount[j + 1] = bmcount[j];
                name[j] = t1; count[j] = t2; bmcount[j] = t3;
            }
        }
    }
    
    for (j = 0; j < BOSS; j++) {
        dhondt_arr[0][j] = bmcount[j];
    }

    printf("\n%% after sort %%");
    printf("\nname        : ");
    for (j = 0; j < BOSS; j++) printf("%d ", name[j]);
    printf("\ncount       : ");
    for (j = 0; j < BOSS; j++) printf("%d ", count[j]);
    printf("\nmax+1-count : ");
    for (j = 0; j < BOSS; j++) printf("%d ", bmcount[j]);

    for (i = 1; i < DIV_MAX; i++) {
        for (j = 0; j < BOSS; j++) {
            dhondt_arr[i][j] = dhondt_arr[0][j] / (i + 1);
        }
    }

    printf("\n%% dhondt conclusion %%\n");
    for (i = 0; i < DIV_MAX; i++) {
        printf("div(%d) ", i + 1);
        for (j = 0; j < BOSS; j++) {
            printf("%d ", dhondt_arr[i][j]);
        }
        putchar('\n');
    }

    allocation = LOT;
    for (k = max + 1; k >= 0; k--) {
        t4 = 0;
        for (i = 0; i < DIV_MAX; i++) {
            for (j = BOSS - 1; j >= 0; j--) {
                if (dhondt_arr[i][j] == k) t4++;
            }
        }
        printf("%d:%d  ", k, t4);
        if (allocation >= t4) {
            for (i = 0; i < DIV_MAX; i++) {
                for (j = BOSS - 1; j >= 0; j--) {
                    if (dhondt_arr[i][j] == k) {
                        allocation--;
                        alloc_arr[name[j]]++;
                        if (allocation == 0) goto end;
                    }
                }
            }
        } else {
            for (j = BOSS - 1; j >= BOSS - t5; j--) {
                allocation--;
                alloc_arr[name[j]]++;
                if (allocation == 0) goto end;
            }
            t5 = allocation;
        }
    }
    end:
    printf("\n%% after allocation %%");
    printf("\nname       : ");
    for (j = 0; j < BOSS; j++) printf("%d ", j);
    printf("\nallocation : ");
    for (j = 0; j < BOSS; j++) {
        printf("%d ", alloc_arr[j]);
        b[j] = alloc_arr[j];
    }
    putchar('\n');
}

