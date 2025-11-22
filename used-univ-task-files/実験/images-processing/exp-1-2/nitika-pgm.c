// pgm ファイルから画像データの読込とファイルへの書き出し

#include <stdio.h>
#include <string.h>

int main(void){
  FILE *fpi=NULL;  // 入力画像
  FILE *fpo=NULL;  // 出力画像

  char buf[258];
  int width, height; // 画像の横長と縦長
  int maxvalue;  // 画像の最大輝度値
  int i, x, y;
  char fnamein[50];
  char fnamein_buf[50];
  char fnameout[50];
  char fnameout_right[10];
  char *tok;
  int pro;


  
  puts("what name is your file? Example name.pgm");
  scanf("%s", fnamein);
  for(i = 0; i<sizeof(fnamein_buf); i++) {
    fnamein_buf[i] = fnamein[i];
  }
  tok = strtok(fnamein_buf, ".");
  sprintf(fnameout, "%s-out.", tok);
  tok = strtok(NULL, ".");
  sprintf(fnameout_right, "%s", tok);
  strcat(fnameout, fnameout_right);

  //ファイル fnamein から画像データを読込む
  fpi=fopen(fnamein, "r");

  if(fpi==NULL)
    printf("Reading file open error ! %s\n", fnamein);

  do  {
       fgets(buf, 256, fpi);
  } while(buf[0] == '#');  // skip over comments

  if(buf[0] != 'P' || buf[1] != '2') {
    fclose(fpi);
    printf(" \n The input image is not pgm format! \n\n");
    return -1;
  }   

  do   {
       fgets(buf , 256 , fpi) ;
   } while(buf [0 ] == '#') ;  // skip over comments

  sscanf(buf , "%d %d" , &width , &height) ; // 画像サイズを読み込む
  fscanf(fpi , "%d" , &maxvalue) ; //最大の輝度値を読み込み

  int img[height][width];
  int array[256] = {0};

  for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      fscanf(fpi, "%d", &img[y][x]);
      array[img[y][x]]++;
    }
  }   //    画像中(x,y)の位置にある画素をimg[y][x]で参照できる。

  fclose(fpi);

  puts("write black proportion in output photo. Example 70");
  scanf("%d", &pro);
  if(pro > 100 || pro < 0) {
    printf("invalid proportion number. at least 0 at most 100");
    return -1;
  }

  int totalImg = height * width;
  int countW = totalImg * ((double) pro / 100);
  i = 0;

  while(countW >= 0 && i < 256) {
    
    countW -= array[i];
    i++;
  }
  
  for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      img[y][x] = img[y][x] > i ? 1 : 0;
    }
  }
  maxvalue = 1;

  //画像データに対する処理, 各要素の値を＋30
  //ただし、画素値が maxvalue より大きい場合、maxvalueにする
 /*
 for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      img[y][x] = img[y][x]+30;
      if(img[y][x] > maxvalue)
	img[y][x] = maxvalue; 
    }
 }
*/
 //画像データをファイル fnameout に出力する

  fpo=fopen(fnameout, "w");

  if(fpo==NULL)
    printf("Reading file open error ! %s\n", fnameout); 

  fprintf(fpo, "P2\n") ;
  fprintf(fpo, "# %s\n", fnameout);
  fprintf(fpo, "%d %d", width, height);
  fprintf(fpo, " \n");
  fprintf(fpo, "%d", maxvalue);
  fprintf(fpo, " \n");

  int count=0; 

  for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      fprintf(fpo, "%3d ", img[y][x]);
      count++;
      if(count%18 == 0){
	      fprintf(fpo, "\n");  //1行18個画素
      }
    }
  }
  fclose(fpo) ;  // 出力画像をクローズする
}
