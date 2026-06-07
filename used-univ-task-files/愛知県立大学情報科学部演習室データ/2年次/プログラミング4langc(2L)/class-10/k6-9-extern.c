#include <stdio.h>

void rev_intary(int v[], int n) {
    int temp[n];
    for (int i = 0; i < n; i++) temp[n - 1 - i] = v[i];
    for (int i = 0; i < n; i++) v[i] = temp[i];
}