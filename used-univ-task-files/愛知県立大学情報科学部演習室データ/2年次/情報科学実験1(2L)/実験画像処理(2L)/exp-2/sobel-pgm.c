//  pgm ファイルから画像データの読込とファイルへの書き出し

#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>

#define DEGREE_OF_EDGE 1

int main(void) {
    FILE *fpi = NULL; //  入力画像
    FILE *fpo = NULL; //  出力画像

    char buf[258];
    int width, height; //  画像の横長と縦長
    int maxvalue;      //  画像の最大輝度値
    int i, j, x, y;
    char fnamein[50];
    char fnamein_buf[50];
    char fnameout[50];
    char fnameout_right[10];
    char *tok;

    puts("what name is your file? Example name.pgm");
    scanf("%s", fnamein);
    for (i = 0; i < sizeof(fnamein_buf); i++) {
        fnamein_buf[i] = fnamein[i];
    }
    tok = strtok(fnamein_buf, ".");
    sprintf(fnameout, "%s-sobel-out.", tok);
    tok = strtok(NULL, ".");
    sprintf(fnameout_right, "%s", tok);
    strcat(fnameout, fnameout_right);

    //  ファイル fnamein から画像データを読込む
    fpi = fopen(fnamein, "r");

    if (fpi == NULL) printf("Reading file open error ! %s\n", fnamein);

    do {
        fgets(buf, 256, fpi);
    } while (buf[0] == '#'); //  skip over comments

    if (buf[0] != 'P' || buf[1] != '2') {
        fclose(fpi);
        printf(" \n The input image is not pgm format! \n\n");
        return -1;
    }

    do {
        fgets(buf, 256, fpi);
    } while (buf[0] == '#'); //  skip over comments

    sscanf(buf, "%d %d", &width, &height); //  画像サイズを読み込む
    fscanf(fpi, "%d", &maxvalue);          //  最大の輝度値を読み込み

    int **img;
    int **bufArr;
    img = (int **)calloc(height, sizeof(int *));
    bufArr = (int **)calloc(height, sizeof(int *));
    for (i = 0; i < height; i++) {
        img[i] = (int *)calloc(width, sizeof(int));
        bufArr[i] = (int *)calloc(width, sizeof(int));
    }
    if (img == NULL || bufArr == NULL) { 
        printf("failed to get enough of memory\n");
        return -1;
    }

    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            fscanf(fpi, "%d", &img[y][x]);
            bufArr[y][x] = 0;
        }
    } //     画像中(x,y)の位置にある画素をimg[y][x]で参照できる。

    fclose(fpi);

    int x_l, y_u;
    int x_r, y_d;
    int value_sum, value_sum_vert, value_sum_hori;
    int weight;
    int sobel_vert[3][3] = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}}; //  tate
    int sobel_hori[3][3] = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}; //  yoko

    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            value_sum = value_sum_vert = value_sum_hori = weight = 0;
            x_l = y_u = -1;
            x_r = y_d = 1;
            if (x == 0) x_l = 0;
            if (x == width - 1) x_r = 0;
            if (y == 0) y_u = 0;
            if (y == height - 1) y_d = 0;

            for (j = y_u; j <= y_d; j++) {
                for (i = x_l; i <= x_r; i++) {
                    weight = sobel_vert[j + 1][i + 1] * DEGREE_OF_EDGE;
                    value_sum_vert += (weight * img[y + j][x + i]);
                    weight = sobel_hori[j + 1][i + 1] * DEGREE_OF_EDGE;
                    value_sum_hori += (weight * img[y + j][x + i]);
                    if (x == 50 && y == 50) printf("vert : %d, hori : %d, img : %d\n", value_sum_vert, value_sum_hori, img[y + j][x + i]);
                }
            }
            value_sum = (int)sqrt(value_sum_vert * value_sum_vert + value_sum_hori * value_sum_hori);
            value_sum = (value_sum > maxvalue) ? maxvalue : value_sum;
            bufArr[y][x] = (value_sum < 0) ? 0 : value_sum;
        }
    }

    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            img[y][x] = bufArr[y][x];
        }
    }

    //  画像データに対する処理, 各要素の値を＋30
    //  ただし、画素値が maxvalue より大きい場合、maxvalueにする
    /*
    for(y=0; y<height; y++) {
       for(x=0; x<width; x++) {
         img[y][x] = img[y][x]+30;
         if(img[y][x] > maxvalue)
     img[y][x] = maxvalue;
       }
    }
    */
    //  画像データをファイル fnameout に出力する

    fpo = fopen(fnameout, "w");

    if (fpo == NULL) printf("Reading file open error ! %s\n", fnameout);

    fprintf(fpo, "P2\n");
    fprintf(fpo, "# %s\n", fnameout);
    fprintf(fpo, "%d %d", width, height);
    fprintf(fpo, " \n");
    fprintf(fpo, "%d", maxvalue);
    fprintf(fpo, " \n");

    int count = 0;

    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            fprintf(fpo, "%3d ", img[y][x]);
            count++;
            if (count % 18 == 0) {
                fprintf(fpo, "\n"); //  1行18個画素
            }
        }
    }
    fclose(fpo); //  出力画像をクローズする

    for (i = 0; i < height; i++) {
        free(img[i]);
        free(bufArr[i]);
    }
    free(img);
    free(bufArr);
}
