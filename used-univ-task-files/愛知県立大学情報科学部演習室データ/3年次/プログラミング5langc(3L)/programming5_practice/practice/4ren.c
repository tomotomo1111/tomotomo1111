/* CやC++などシェルに実行結果コード返却を明示する言語を利用する場合 基本的に0を返却してください。 */
#include <stdio.h>

typedef struct bunsu{
    int bunshi;
    int bunbo;
} bunsu;

void cal(bunsu *bun1, bunsu *bun3);
int calG(int a, int b);

int main() {
    char s[100];
    FILE *fp;
    scanf("%99s", s);
    if ((fp = fopen(s, "rb")) == NULL) {
        puts("ファイルが存在しません");
        return 0;
    }
    int arr[5]; 
    if (fread(arr, sizeof(int), 4, fp) == 0) return 0;
    bunsu bunsu1 = {arr[0], arr[1]};
    bunsu bunsu2 = {arr[2], arr[3]};
    bunsu *bunsu3;
    bunsu *bunsu4;
    if (arr[0] == 0) return 0;
    if (arr[2] == 0) return 0;
    cal(&bunsu1, bunsu3);
    // cal(&bunsu2, bunsu4);
    if (bunsu3->bunbo == 1 && bunsu4->bunbo == 1) {
        printf("%d, %d", bunsu3->bunshi, bunsu4->bunshi);
    } else if(bunsu4->bunbo == 1) {
        printf("%d/%d, %d", bunsu3->bunshi, bunsu3->bunbo, bunsu4->bunshi);
    } else if(bunsu3->bunbo == 1) {
        printf("%d, %d/%d", bunsu3->bunshi, bunsu4->bunshi, bunsu4->bunbo);
    } else {
        printf("%d/%d, %d/%d", bunsu3->bunshi, bunsu3->bunbo, bunsu4->bunshi, bunsu4->bunbo);
    }

    fclose(fp);
    return 0;
}

void cal(bunsu *bun1, bunsu *bun3) {
    int p = calG(bun1->bunshi, bun1->bunbo);
    bun3->bunshi = (bun1->bunshi) / p;
    bun3->bunbo = (bun1->bunbo) / p;
}

int calG(int a, int b) {
    int p = a;
    int q = b;
    if (a < b) {
        p = b;
        q = a;
    }
    int r;
    while (r != 0) {
        r = p % q;
        p = q;
        q = r;
    }
    return p;
}