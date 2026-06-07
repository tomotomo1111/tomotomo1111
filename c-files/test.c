/* CやC++などシェルに実行結果コード返却を明示する言語を利用する場合 基本的に0を返却してください。 */
#include <stdio.h>

int main()
{
    char n[100], p[100], d[100];
    scanf("%s %s", n, p);
    scanf("%s", d);
    int ni = atoi(n); int pi = atoi(p); int di = atoi(d);
    int temp = 0;
    for (int j = 0; j < di; j++) {
        if (ni < pi) {
            temp = ni;
            while (ni < pi) ni += temp;
        }
        ni -= pi;
    }
    printf("%d", ni);
    return 0;
}