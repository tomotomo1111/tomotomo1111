/* バグの例3(型・論理ミスなど)　デバッグ済 */
/* 2つの対角線の値からひし形の面積を求める */
#include <stdio.h>

int main(void)
{
    int taikaku1, taikaku2;
    double  menseki;
    
    puts("ひし形の面積を計算します");
    printf("対角線の1つ目の値の入力: ");
    scanf("%d", &taikaku1);
    
    printf("対角線の2つ目の値の入力: ");
    scanf("%d", &taikaku2);
    
    menseki = taikaku1 * taikaku2 / 2.0;
    
    printf("対角線%dと%dのひし形の面積は%.1fです\n", taikaku1, taikaku2, menseki);
    
    return 0;
}
