#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define MAX 10000
#define DAMAGE_BONUS 4
#define USE_CRITIAL_TO_DOUBLE_DAMAGE_OR_DISABLE_AVOIDANCE 1 // 1 : クリティカルを回避不可にしようする。2つクリ出たならダメ2倍と回避不可。 0 : クリティカルを全てダメージ2倍にしようする
#define MARTIAL_ARTS_WILL_DOUBLE_DAMAGE 1

int max, damage_bonus, use_critial_to_double_damage_or_disable_avoidance, martial_arts_will_double_damage;

// return 0 -> false
//        1 -> true
int isInSection(int check_num, int min_num, int max_num) {
    if (check_num >= min_num && check_num <= max_num) return 1;
    return 0;
}

// mrandom(1, 100) -> 1d100 , mrandom(1, 4) -> 1d4 となる
int mrandom(int min_num, int max_num) {
    static int frag;

    if (min_num >= max_num) return 0;

    if (frag == 0) {
        srand((unsigned int)time(NULL));
        frag = 1;
    }
    
    return min_num + (int) (rand() * (max_num - min_num + 1.0) / (RAND_MAX + 1.0));
}

// 不適切な値なら-1を返す。期待値の収束値を返す。
float calExpectedVal(int kick_p, int martial_arts_p, int showCalProcess) {

    if (!isInSection(martial_arts_p, 0, 100) || !isInSection(kick_p, 0, 100) || !isInSection(damage_bonus, 0, 6)) return -1;
    
    int damage_total = 0;
    int damage_tmp = 0;
    int kick_dice = 0;
    int martial_arts_dice = 0;
    int damage_kick_output = 0;
    int damage_bonus_output = 0;
    int isDoubled = 0;

    for (int j = 1; j <= max; j++) {
        
        damage_tmp = kick_dice = martial_arts_dice = damage_kick_output = damage_bonus_output = isDoubled = 0;

        // ダイスロール
        kick_dice = mrandom(1, 100);
        martial_arts_dice = mrandom(1, 100);

        // キック成功したかどうか
        if (isInSection(kick_dice, 1, kick_p)) {
            // キックのダメージ
            damage_tmp = mrandom(1, 6);
            
            // キック成功してマーシャルアーツも成功したかどうか
            isDoubled = isInSection(martial_arts_dice, 1, martial_arts_p);
            if (isDoubled && martial_arts_will_double_damage) damage_tmp *= 2;
            if (isDoubled && !martial_arts_will_double_damage) damage_tmp += mrandom(1, 6);;

            // キックとマーシャルアーツが両方ともクリティカルなら回避不可とダメ2倍とする
            if (!use_critial_to_double_damage_or_disable_avoidance && (isInSection(kick_dice, 1, 5) || isInSection(martial_arts_dice, 1, 5))) damage_tmp *= 2;
            if (isInSection(kick_dice, 1, 5) && isInSection(martial_arts_dice, 1, 5)) damage_tmp *= 2;

            // キックとダメボの合算する
            damage_kick_output = damage_tmp;
            damage_bonus_output = mrandom(1, damage_bonus);
            damage_tmp += damage_bonus_output; // damage_bunus4なら (2(1d6) + 1d4) or (1d6 + 1d4)
            
        }

        damage_total += damage_tmp; // damage_bunus4なら (2(1d6) + 1d4) or (1d6 + 1d4) or (0)

        if (showCalProcess) printf("試行回数:%d, キック(%d)%02d, マーシャルアーツ(%d)%02d, ダメージ:%d+%d=%d, 通算期待値:%5.2f \n", j, kick_p, kick_dice, martial_arts_p, martial_arts_dice, damage_kick_output, damage_bonus_output, damage_tmp, damage_total / (float)j);
    }

    return damage_total / (float)max;
}

void manualCal() {
    int martial_arts_p, kick_p;
    martial_arts_p = kick_p = 0;
    
    printf("\n---------------------------------------------------------------");
    do {
        puts("\n「キック」 「マーシャルアーツ」 の技能値を整数値で入力してください。");
        printf("\nキックの技能値(0~100) :");
        scanf("%d", &kick_p);
        printf("\nマーシャルアーツの技能値(0~100) : ");
        scanf("%d", &martial_arts_p);
    } while (!isInSection(martial_arts_p, 0, 100) || !isInSection(kick_p, 0, 100));
    printf("キック技能値 : %d, マーシャルアーツ技能値 : %d", kick_p, martial_arts_p);
    printf("\nクリティカルは%s、マーシャルアーツは%s\n", use_critial_to_double_damage_or_disable_avoidance?"回避優先":"ダメージ優先", martial_arts_will_double_damage?"ダメージ2倍":"ダイス2回");
    calExpectedVal(kick_p, martial_arts_p, 1);
    printf("\n---------------------------------------------------------------\n\n");
}

void autoCal(){
    int martial_arts_p_min, martial_arts_p_max, kick_p_min, kick_p_max, d;
    martial_arts_p_min = martial_arts_p_max = kick_p_min = kick_p_max = d = 0;

    printf("\n---------------------------------------------------------------");
    do {
        puts("\nキック・マーシャルアーツ技能値の開始値と終了値、差分を入力してください");
        printf("\nキック技能値の開始値(25~100) :");
        scanf("%d", &kick_p_min);
        printf("\nキック技能値の終了値(25~100) : ");
        scanf("%d", &kick_p_max);
        printf("\nマーシャルアーツ技能値の開始値(1~100) :");
        scanf("%d", &martial_arts_p_min);
        printf("\nマーシャルアーツ技能値の終了値(1~100) : ");
        scanf("%d", &martial_arts_p_max);
        printf("\n差分の値(1~25) : ");
        scanf("%d", &d);
    } while (!isInSection(kick_p_min, 25, 100) || !isInSection(kick_p_max, 25, 100) || !isInSection(damage_bonus, 0, 6) || !isInSection(martial_arts_p_min, 1, 100) || !isInSection(martial_arts_p_max, 1, 100) || !isInSection(d, 1, 25));
    printf("\nキック範囲 : %d ~ %d, マーシャルアーツ範囲 : %d ~ %d, ダメボ : 1d%d, 差分 : %d\n", kick_p_min, kick_p_max, martial_arts_p_min, martial_arts_p_max, damage_bonus, d);
    printf("\nクリティカルは%s、マーシャルアーツは%s\n", use_critial_to_double_damage_or_disable_avoidance?"回避優先":"ダメージ優先", martial_arts_will_double_damage?"ダメージ2倍":"ダイス2回");
    for (int j = kick_p_min; j <= kick_p_max; j += d) {
        for (int i = martial_arts_p_min; i <= martial_arts_p_max; i += d) {
            printf("キック:%d, マーシャルアーツ:%d, 期待値収束(%d試行):%f\n", j, i, max, calExpectedVal(j, i, 0));
        }
        puts("");
    }
    printf("\n---------------------------------------------------------------\n\n");
}

void setParameter() {
    printf("\n---------------------------------------------------------------");
    printf("\nパラメータセット...");
    int tmp_m = 0;
    int tmp_d = 0;
    int tmp_mo = 0;
    int tmp_mmi = 0;
    do {
        printf("\n試行回数の値(100~100000) : ");
        scanf("%d", &tmp_m);
        printf("\nダメージボーナスの値(0~6, 0はダメボなし) : 1d");
        scanf("%d", &tmp_d);
        printf("\nクリティカル2つで回避不可とダメ2倍。1つだと回避不可優先にする : 1, クリティカルは必ずダメ2倍として : 0\n");
        scanf("%d", &tmp_mo);
        printf("\nマーシャルアーツはダメージ2倍 : 1, ダイス2回の合計 : 0\n");
        scanf("%d", &tmp_mmi);
    } while (!isInSection(tmp_m, 100, 100000) || !isInSection(tmp_d, 0, 6) || !isInSection(tmp_mo, 0, 1) || !isInSection(tmp_mmi, 0, 1));
    printf("\n試行回数 : (前)%d -> (後)%d", max, tmp_m);
    printf("\nダメージボーナス : (前)%d -> (後)%d", damage_bonus, tmp_d);
    printf("\nクリティカルは回避不可優先かダメージ優先か : (前)%s -> (後)%s", use_critial_to_double_damage_or_disable_avoidance?"回避優先":"ダメージ優先", tmp_mo?"回避優先":"ダメージ優先");
    printf("\nマーシャルアーツはダメージ2倍かダイス2回の合計か : (前)%s -> (後)%s", martial_arts_will_double_damage?"ダメージ2倍":"ダイス2回", tmp_mmi?"ダメージ2倍":"ダイス2回");
    printf("\n---------------------------------------------------------------\n\n");
    max = tmp_m;
    damage_bonus = tmp_d;
    use_critial_to_double_damage_or_disable_avoidance = tmp_mo;
    martial_arts_will_double_damage = tmp_mmi;
}

void resetParameter() {
    
    printf("\n---------------------------------------------------------------");
    printf("\nパラメータリセット...");
    printf("\n試行回数 : (前)%d -> (後)%d", max, MAX);
    printf("\nダメージボーナス : (前)%d -> (後)%d", damage_bonus, DAMAGE_BONUS);
    printf("\nクリティカルは回避不可優先かダメージ優先か : (前)%s -> (後)%s", use_critial_to_double_damage_or_disable_avoidance?"回避優先":"ダメージ優先", USE_CRITIAL_TO_DOUBLE_DAMAGE_OR_DISABLE_AVOIDANCE?"回避優先":"ダメージ優先");
    printf("\nマーシャルアーツはダメージ2倍かダイス2回の合計か : (前)%s -> (後)%s", martial_arts_will_double_damage?"ダメージ2倍":"ダイス2回", MARTIAL_ARTS_WILL_DOUBLE_DAMAGE?"ダメージ2倍":"ダイス2回");
    printf("\n---------------------------------------------------------------\n\n");
    max = MAX;
    damage_bonus = DAMAGE_BONUS;
    use_critial_to_double_damage_or_disable_avoidance = USE_CRITIAL_TO_DOUBLE_DAMAGE_OR_DISABLE_AVOIDANCE;
    martial_arts_will_double_damage = MARTIAL_ARTS_WILL_DOUBLE_DAMAGE;
}

// コメント解除して、下の二重for文をコメントすれば任意にできる
int main(void) {
    
    int isRunning = 1;
    char input_c;

    max = MAX;
    damage_bonus = DAMAGE_BONUS;

    do {
        puts("\n技能値固定で収束過程を表示 : m, 技能値が自動変動の結果のみ表示 : a, パラメータ設定 : s, パラメータリセット : r, 終了 : q\n");
        input_c = getchar();
        if (input_c == 'q') isRunning = 0;
        if (input_c == 'm') manualCal();
        if (input_c == 's') setParameter();
        if (input_c == 'a') autoCal();
        if (input_c == 'r') resetParameter();
    } while (isRunning);

    return 0;
}