#include <stdio.h>
int myPow(int x, int n) {
    int ans = 1;
    for (int i = 0; i < n; i++) ans *= x;
    return ans;
}

int main(void) {
    printf("ans : %d", myPow(5,3));
}