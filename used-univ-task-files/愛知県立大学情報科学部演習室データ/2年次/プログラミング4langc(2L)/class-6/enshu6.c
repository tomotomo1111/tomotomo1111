/*

入力0 : 1
入力1 : 2
入力2 : 3
入力3 : 4
入力4 : 5
入力5 : 6
入力6 : -0
平均 : 3.50, 最大 : 6, 最小 : 1

*/
#include <stdio.h>

#define True 1
int main(void) {
    int sum, min, max;
    sum = min = max = 0;
    int i = 0;
    int input = 0;

    while (True) {
        printf("入力%d : ", i);
        scanf("%d", &input);
        if (input <= 0) break;
        if(i == 0) {
            min = max = sum = input;
        } else {
            sum += input;
            if (min > input) min = input;
            if (max < input) max = input;
        }
        i++;
    }
    printf("平均 : %.2f, 最大 : %d, 最小 : %d\n", (double) sum / i, max, min);
    return 0;
}