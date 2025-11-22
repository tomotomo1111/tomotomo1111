// pbm ファイルから画像データの読込とファイルへの書き出し

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define DIRECTION_MAX_INTEGER 8

int main(void)
{
  FILE *fpi = NULL; // 入力画像
  FILE *fpo = NULL; // 出力画像

  char buf[258];
  int width, height; // 画像の横長と縦長
  int maxvalue;      // 画像の最大輝度値
  int i, x, y;
  char fnamein[50];
  char fnamein_buf[50];
  char fnameout[50];
  char fnameout_right[10];
  char *tok;
  int pro;

  puts("what name is your file? Example name.pbm");
  scanf("%s", fnamein);
  for (i = 0; i < sizeof(fnamein_buf); i++) {
    fnamein_buf[i] = fnamein[i];
  }
  tok = strtok(fnamein_buf, ".");
  sprintf(fnameout, "%s-out.", tok);
  tok = strtok(NULL, ".");
  sprintf(fnameout_right, "%s", tok);
  strcat(fnameout, fnameout_right);

  // ファイル fnamein から画像データを読込む

  fpi = fopen(fnamein, "r");

  if (fpi == NULL) printf("Reading file open error ! %s\n", fnamein);

  do {
    fgets(buf, 256, fpi);
  } while (buf[0] == '#'); // skip over comments

  if (buf[0] != 'P' || buf[1] != '1') {
    fclose(fpi);
    printf(" \n The input image is not pbm format! \n\n");
    return -1;
  }

  do {
    fgets(buf, 256, fpi);
  } while (buf[0] == '#'); // skip over comments

  sscanf(buf, "%d %d", &width, &height); // 画像サイズを読み込む

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
  } //    画像中(x,y)の位置にある画素をimg[y][x]で参照できる。

  fclose(fpi);

  int dir_current = 6;
  int dir_next = 4;
  int skip_frag;
  int x_c;
  int y_c;
  int p_c = 0; // 色 1 黒 0 白
  int length = 0;
  for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
      if (img[y][x] && !bufArr[y][x] && !p_c) {
        bufArr[y][x] = 1;
        x_c = x;
        y_c = y;
        dir_current = 6;
        length = 0;
        do {
          skip_frag = 0;
          dir_next = (dir_current - 2 + DIRECTION_MAX_INTEGER) % DIRECTION_MAX_INTEGER;
          for (i = 0; i < DIRECTION_MAX_INTEGER; i++) {
            if (i)
              dir_next = (dir_next + 1 + DIRECTION_MAX_INTEGER) % DIRECTION_MAX_INTEGER;
            switch (dir_next) {
            case 0:
              if (x_c < width - 1 && img[y_c + 0][x_c + 1]) {
                x_c++;
                skip_frag++;
              }
              break;
            case 1:
              if (x_c < width - 1 && y_c > 0 && img[y_c - 1][x_c + 1]) {
                x_c++;
                y_c--;
                skip_frag++;
              }
              break;
            case 2:
              if (y_c > 0 && img[y_c - 1][x_c + 0]) {
                y_c--;
                skip_frag++;
              }
              break;
            case 3:
              if (x_c > 0 && y_c > 0 && img[y_c - 1][x_c - 1]) {
                x_c--;
                y_c--;
                skip_frag++;
              }
              break;
            case 4:
              if (x_c > 0 && img[y_c + 0][x_c - 1]) {
                x_c--;
                skip_frag++;
              }
              break;
            case 5:
              if (x_c > 0 && y_c < height - 1 && img[y_c + 1][x_c - 1]) {
                x_c--;
                y_c++;
                skip_frag++;
              }
              break;
            case 6:
              if (y_c < height - 1 && img[y_c + 1][x_c + 0]) {
                y_c++;
                skip_frag++;
              }
              break;
            case 7:
              if (x_c < width - 1 && y_c < height - 1 && img[y_c + 1][x_c + 1]) {
                x_c++;
                y_c++;
                skip_frag++;
              }
              break;
            }
            if (skip_frag) {
              bufArr[y_c][x_c] = 1;
              dir_current = dir_next;
              length++;
              break;
            }
          }
        } while (x_c != x || y_c != y);
        printf("%d\n", length);
      }
      p_c = img[y][x] ? 1 : 0;
    }
  }

  for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
      img[y][x] = bufArr[y][x];
    }
  }

  // 画像データに対する処理, 0,1 逆転
  /*
  for (y = 0; y < height; y++)
  {
    for (x = 0; x < width; x++)
    {
      if (img[y][x] == 1)
        img[y][x] = 0;
      else
        img[y][x] = 1;
    }
  }
  */
  // 画像データをファイル fnameout に出力する

  fpo = fopen(fnameout, "w");

  if (fpo == NULL) printf("Reading file open error ! %s\n", fnameout);

  fprintf(fpo, "P1\n");
  fprintf(fpo, "# %s\n", fnameout);
  fprintf(fpo, "%d %d \n", width, height);

  int count = 0;

  for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
      fprintf(fpo, "%1d ", img[y][x]);
      count++;
      if (count % 18 == 0) {
        fprintf(fpo, "\n"); // 1行18個画素
      }
    }
  }
  fclose(fpo); // 出力画像をクローズする

  for (i = 0; i < height; i++) {
    free(img[i]);
    free(bufArr[i]);
  }
  free(img);
  free(bufArr);
}
