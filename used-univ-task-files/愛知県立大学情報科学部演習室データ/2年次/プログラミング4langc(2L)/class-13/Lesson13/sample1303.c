/* 配列の全要素をゼロにする */
#include <stdio.h>

/*--- 要素数nの配列vの要素に0を代入 ---*/
void set_zero(int v[], int n)
{
    int i;

    for (i = 0; i < n; i++)
        v[i] = 0;
}

/*--- 要素数nの配列vの全要素を表示して改行 ---*/
void print_array(const int v[], int n)
{
    int i;

    printf("{ ");
    for (i = 0; i < n; i++)
        printf("%d ", v[i]);
    printf("}");
}

int main(void)
{
    int ary1[] = {1, 2, 3, 4, 5, 2, 0};
    int ary2[] = {3, 2, 1};
    int len1 = sizeof(ary1)/sizeof(ary1[0]);
    int len2 = sizeof(ary2)/sizeof(ary2[0]);

    printf("ary1 = ");   print_array(ary1, len1);   putchar('\n'); // 長さ対応させる変更
    printf("ary2 = ");   print_array(ary2, len2);   putchar('\n');

    set_zero(ary1, len1);      /* 配列ary1の全要素に0を代入 */
    set_zero(ary2, len2);      /* 配列ary2の全要素に0を代入 */

    printf("両配列の全要素に0を代入しました。\n");
    printf("ary1 = ");   print_array(ary1, len1);   putchar('\n');
    printf("ary2 = ");   print_array(ary2, len2);   putchar('\n');

    return 0;
}
