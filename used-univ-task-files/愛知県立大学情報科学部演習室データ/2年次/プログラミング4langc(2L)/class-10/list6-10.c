#include <stdio.h>

#define NUMBER 5

int top(void) {
    extern int tensu[];
    int max = tensu[0];

    for (int i = 0; i < NUMBER; i++) {
        if (tensu[i] > max) max = tensu[i];
    }
    return max;
}