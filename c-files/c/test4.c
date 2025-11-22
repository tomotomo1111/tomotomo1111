/* CやC++などシェルに実行結果コード返却を明示する言語を利用する場合 基本的に0を返却してください。 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int isNum(char *, int);

int main() {
    int q, n, j, i;
    char c;
    char str[1000];
    scanf("%d %c %d", &q, &c, &n);
    str[0] = c;
    char **a;
    a = (char **) malloc(sizeof(char *) * q);
    for (j = 0; j < q; j++) a[j] = (char *) malloc(sizeof(char) * 20);
    for (j = 0; j < q; j++) scanf("%s", &a[j]);
    if (isNum(a[j], strlen(a[j])) == 1) {
        n += atoi(a[j]);
    } else {
        strcat(str, a[j]);
    }
    
    printf("%s\n", str);
    for (j = 0; j < q; j++) free(a[j]);
    free(a);
    return 0;
}

int isNum(char *str, int len) {
    int j; int isNum = 1;
    for (j = 0; j < len; j++) {
        if (str[j] < '0' || str[j] > '9') isNum = 0;
    }
    return isNum;
}