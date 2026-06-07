// pbm ファイルから画像データの読込とファイルへの書き出し
#define _USE_MATH_DEFINES
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#define DIRECTION_MAX_INTEGER 8

double followOutlineAndGetLength(int** ori, int** buf, int width, int height, int* dc, int x, int y, int label) {
    int dn;
    int x_c = x;
    int y_c = y;
    double length = 0;
    int vec[8][6] = {{-1,width-1,-1,height,1,0},{-1,width-1,0,height,1,-1},{-1,width,0,height,0,-1},{0,width,0,height,-1,-1},
                    {0,width,-1,height,-1,0},{0,width,-1,height-1,-1,1},{-1,width,-1,height-1,0,1},{-1,width-1,-1,height-1,1,1}};
    do {
        dn = (*dc - 2 + DIRECTION_MAX_INTEGER) % DIRECTION_MAX_INTEGER;
        for (int i = 0; i < DIRECTION_MAX_INTEGER; i++) {
            if(i) dn = (dn + 1 + DIRECTION_MAX_INTEGER) % DIRECTION_MAX_INTEGER;
            if(x_c > vec[dn][0] && x_c < vec[dn][1] && y_c > vec[dn][2] && y_c < vec[dn][3] && ori[y_c + vec[dn][5]][x_c + vec[dn][4]]){
                x_c += vec[dn][4];
                y_c += vec[dn][5];
                buf[y_c][x_c] = label;
                length += (dn % 2 == 0) ? 1 : sqrt(2);
                *dc = dn;
                break;
            }
        }
    } while (x_c != x || y_c != y);
    return length;
}

void fillInside(int** ori, int** buf, int width, int height) {
    int x, y;
    for (y = 1; y < height; y++) {
        for (x = 1; x < width; x++) {
            if(!buf[y][x] && buf[y][x-1] && ori[y][x]) buf[y][x] = buf[y][x-1];
            if(!buf[y][x] && buf[y-1][x] && ori[y][x]) buf[y][x] = buf[y-1][x];
        }
    }
}

int getnumTotal(int** buf, int label, int width, int height) {
    int x, y;
    int count = 0;
    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            if(buf[y][x] == label) count++;
        }
    }
    return count;
}

int getCPX(int** buf, int label, int width, int height) {
    int x, y;
    int sumx = 0;
    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            if(buf[y][x] == label) sumx += x;
        }
    }
    return sumx / getnumTotal(buf, label, width, height);
}

int getCPY(int** buf, int label, int width, int height) {
    int x, y;
    int sumy = 0;
    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            if(buf[y][x] == label) sumy += y;
        }
    }
    return sumy / getnumTotal(buf, label, width, height);
}

double calC(int numTotal, double length) {

  return 4.0f * M_PI * numTotal / pow(length, 2.0f);
}

void rmLabel(int** buf, int* label, int width, int height) {
    int x, y;
    for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
            if(buf[y][x] == *label) buf[y][x] = -1;
        }
    }
    *label--;
}

int main(void) {
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

  puts("what name is your file? Example name.pgm");
  scanf("%s", fnamein);
  for (i = 0; i < sizeof(fnamein_buf); i++) {
    fnamein_buf[i] = fnamein[i];
  }
  tok = strtok(fnamein_buf, ".");
  sprintf(fnameout, "%s-circle-out.pgm", tok);

  // ファイル fnamein から画像データを読込む

  fpi = fopen(fnamein, "r");

  if (fpi == NULL) printf("Reading file open error ! %s\n", fnamein);

  do {
    fgets(buf, 256, fpi);
  } while (buf[0] == '#'); // skip over comments

  if (buf[0] != 'P' || buf[1] != '2') {
    fclose(fpi);
    printf(" \n The input image is not pgm format! \n\n");
    return -1;
  }

  do {
    fgets(buf, 256, fpi);
  } while (buf[0] == '#'); // skip over comments

  sscanf(buf, "%d %d", &width, &height); // 画像サイズを読み込む
  fscanf(fpi, "%d", &maxvalue);

  int **img = (int **)calloc(height, sizeof(int *));
  int **bufArr = (int **)calloc(height, sizeof(int *));
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
    }
  } //    画像中(x,y)の位置にある画素をimg[y][x]で参照できる。

  fclose(fpi);

  int dir_current;
  int p_c = 0; // 1 黒 0 白
  int label = 1;
  double length, C_value;
  int total, cpx, cpy;
  for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
      if (img[y][x] && !bufArr[y][x] && !p_c) {
        bufArr[y][x] = 1;
        dir_current = 6;
        length = followOutlineAndGetLength(img, bufArr, width, height, &dir_current, x, y, label);        
        fillInside(img, bufArr, width, height);
        total = getnumTotal(bufArr, label, width, height);
        cpx =  getCPX(bufArr, label, width, height);
        cpy =  getCPY(bufArr, label, width, height);
        C_value = calC(total, length);
        if(C_value < 0.9f) {
            rmLabel(bufArr, &label, width, height);
        } else {
            printf("label : %d, square : %d, length : %5.2f, cpoint : (%d, %d), Cvalue : %5.2f\n", label, total, length, cpx, cpy, C_value);
            label++;
        }
      }
      p_c = img[y][x] ? 1 : 0;
    }
  }
  
  maxvalue = 255;
  for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
        if(bufArr[y][x] == -1) bufArr[y][x] = 0;
        img[y][x] = bufArr[y][x] * (maxvalue / (label - 1));
    }
  }

  // 画像データをファイル fnameout に出力する
  fpo = fopen(fnameout, "w");

  if (fpo == NULL) printf("Reading file open error ! %s\n", fnameout);
  
  fprintf(fpo, "P2\n");
  fprintf(fpo, "# %s\n", fnameout);
  fprintf(fpo, "%d %d \n", width, height);
  fprintf(fpo, "%d\n", maxvalue);

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