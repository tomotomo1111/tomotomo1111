/* CやC++などシェルに実行結果コード返却を明示する言語を利用する場合 基本的に0を返却してください。 */
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main()
{
    char p[100], temp2[100];
    int n, x;
    scanf("%d", &n);
    int A[n];
    int j = 0;
    while (scanf("%d", &A[j]) == 1 && j < n-1) j++;
    scanf("%d", &x);
    bool ans = false;
    for (int i = 0; i < n; i++) {
        if (A[i] % x == 0) {
            ans = true;
        }
    }
    printf("%s", ans ? "YES" : "NO");
    return 0;
}