/*  
    [入力]
        生徒数を入れてください.
        3
        1人目の英語 10
        1人目の数学 20
        1人目の物理 30

        2人目の英語 11
        2人目の数学 12
        2人目の物理 13

        3人目の英語 50
        3人目の数学 40
        3人目の物理 32

    [出力]
        英語の平均点 : 23.67, 分散 : 346.89, 最高点 : 50, 最低点 : 10,
        数学の平均点 : 24.00, 分散 : 138.67, 最高点 : 40, 最低点 : 12,
        物理の平均点 : 25.00, 分散 : 72.67, 最高点 : 32, 最低点 : 13,
*/
#include <stdio.h>
#define SUB 3

double ave(int source[], int n) {
    double sum = 0;
    for (int i = 0; i < n; i++) {
        sum += (double) source[i];
    }
    return sum / n;
}

double var(int source[], int n) {
    double average = ave(source, n); 
    double vsum = 0;
    for (int i = 0; i < n; i++) vsum += (source[i] - average) * (source[i] - average);
    return vsum / n;
}

int max(int source[], int n) {
    int max = source[0];
    for (int i = 0; i < n; i++) if(max < source[i]) max = source[i];
    return max;
}

int min(int source[], int n) {
    int min = source[0];
    for (int i = 0; i < n; i++) if(min > source[i]) min = source[i];
    return min;
}

int main(void) {
    int n;
    puts("生徒数を入れてください.");
    scanf("%d", &n);
    int P[n][SUB];
    char str[4][6] = {"英語", "数学", "物理"};
    for (int j = 0; j < n; j++) {
        for (int i = 0; i < SUB; i++) {
            printf("%d人目の%s ",j+1, str[i]);
            scanf("%d", &P[j][i]);
        }
        putchar('\n');
    }
    putchar('\n');

    int source[n];
    for (int i = 0; i < SUB; i++) {
        for (int j = 0; j < n; j++) {
            source[j] = P[j][i];
        }
        printf("%sの平均点 : %5.2f, 分散 : %5.2f, 最高点 : %d, 最低点 : %d,", str[i], ave(source, n), var(source, n), max(source, n), min(source, n));
        putchar('\n');
    }
}