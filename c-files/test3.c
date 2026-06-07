/* CやC++などシェルに実行結果コード返却を明示する言語を利用する場合 基本的に0を返却してください。 */
#include <stdio.h>
#include <stdlib.h>

int main()
{
    int n, m, u, j, i;
    scanf("%d", &n);
    scanf("%d", &m);
    scanf("%d", &u);
    char **name_tmp; char *name; int name_len_max = 33;
    name_tmp = (char **) malloc(sizeof(char *) * n);
    for (j = 0; j < n; j++) {
        name_tmp[j] = (char *) malloc(sizeof(char) * name_len_max);
    }

    for (j = 0; j < n; j++) scanf("%s", name_tmp[j]);

    int last_i = m / u;
    int golden_drop_i = (m % u == 0) ? last_i - 1 : last_i ;
    printf("%s\n", name_tmp[golden_drop_i % n]);

    for (j = 0; j < n; j++) {
        free(name_tmp[j]);
    }
    free(name_tmp);
    return 0;
}