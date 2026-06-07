/*
じゃんけんゲーム開始!!

じゃんけんポン … (0)グー (1)チョキ (2)パー：2
私はグーで、あなたはパーです。
あなたの勝ちです。

じゃんけんポン … (0)グー (1)チョキ (2)パー：1
私はグーで、あなたはチョキです。
あなたの負けです。

じゃんけんポン … (0)グー (1)チョキ (2)パー：0
私はパーで、あなたはグーです。
あなたの負けです。

じゃんけんポン … (0)グー (1)チョキ (2)パー：2
私はパーで、あなたはパーです。
引き分けです。

じゃんけんポン … (0)グー (1)チョキ (2)パー：1
私はパーで、あなたはチョキです。
あなたの勝ちです。

じゃんけんポン … (0)グー (1)チョキ (2)パー：11

じゃんけんポン … (0)グー (1)チョキ (2)パー：-5

じゃんけんポン … (0)グー (1)チョキ (2)パー：0
私はグーで、あなたはグーです。
引き分けです。

じゃんけんポン … (0)グー (1)チョキ (2)パー：2
私はグーで、あなたはパーです。
あなたの勝ちです。

□あなたの勝ちです。

*/


#include <stdio.h>
#include <stdlib.h>
#include <time.h> // time.h をつけずにシードで time を実行していたので修正神野

int human;      /* 人間の手 */
int comp;       /* コンピュータの手 */
int win_no;     /* 勝った回数 */
int lose_no;    /* 負けた回数 */
int draw_no;    /* 引き分けた回数 */

char *hd[] = {"グー", "チョキ", "パー"};        /* 手 */

/*--- 初期処理 ---*/
void initialize(void)
{
    win_no  = 0;        /* 勝った回数 */
    lose_no = 0;        /* 負けた回数 */ // 負けた回数 1 になってました神野
    draw_no = 0;        /* 引き分けた回数 */

    srand(time(NULL));  /* 乱数の種を初期化 */

    printf("じゃんけんゲーム開始!!\n");
}

/*--- じゃんけん実行（手の読込み／生成） ---*/
void jyanken(void)
{
    int i;

    comp = rand() % 3;      /* コンピュータの手（0～2）を乱数で生成 */ // % 5 は手の数より多いので % 3 に変更した神野

    do {
        printf("\n\aじゃんけんポン …");
        for (i = 0; i < 3; i++)
            printf(" (%d)%s", i, hd[i]);
        printf("：");
        scanf("%d", &human);        /* 人間の手を読み込む */
    } while (human < 0 || human > 2); // 0, 1, 2 以外の数を入力したとき再入力が求められないバグを修正神野
}

/*--- 勝／負／引き分け回数を更新 ---*/
void count_no(int result)
{
    switch (result) {
     case 0: draw_no++;  break;                     /* 引き分け */
     case 1: lose_no++;  break;                     /* 負け */
     case 2: win_no++;   break;                     /* 勝ち */
    }
}

/*--- 判定結果を表示 ---*/
void disp_result(int result)
{
    switch (result) {
     case 0: puts("引き分けです。");       break;   /* 引き分け */
     case 1: puts("あなたの負けです。");   break;   /* 負け */
     case 2: puts("あなたの勝ちです。");   break;   /* 勝ち */
    }
}

int main(void)
{
    int judge;              /* 勝敗 */

    initialize();                            /* 初期処理 */ // セミコロンつけました神野

    do {
        jyanken();                           /* じゃんけん実行 */ // jyankenpon という存在しない関数を jyanken に変更

        /* コンピュータと人間の手を表示 */
        printf("私は%sで、あなたは%sです。\n", hd[comp], hd[human]);

        judge = (human - comp + 3) % 3;     /* 勝敗を判定 */

        count_no(judge);                    /* 勝／負／引き分け回数を更新 */

        disp_result(judge);                  /* 判定結果を表示 */ // 実意引数が書いていないので judge 追加神野

    } while (win_no < 3 && lose_no < 3); // 両方3にならないと終わらないバグ判定を修正神野

    if (win_no == 3)
        printf("\n□あなたの勝ちです。\n");
    else
        printf("\n■私の勝ちです。\n");

    return 0;
}
