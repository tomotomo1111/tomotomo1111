// pbm ファイルから画像データの読込とファイルへの書き出し
// 

#include <stdio.h>
#include <string.h>

int main(void){
  FILE *fpi=NULL;  // 入力画像
  FILE *fpo=NULL;  // 出力画像

  char buf[258];
  int width, height; // 画像の横長と縦長
  int maxvalue;  // 画像の最大輝度値
  int i, x, y;
  char fnamein[128]; 
  char fnameout[128]; 

 printf("\n Please input the file name of the input pbm image :  "); 

  //入力画像名を読み込む
  scanf("%s", fnamein); 


  //ファイル fnamein から画像データを読込む

  fpi=fopen(fnamein, "r");

  if(fpi==NULL)
    printf("Reading file open error ! %s\n", fnamein);
   
  do  {
       fgets(buf, 256, fpi);
  } while(buf[0] == '#');  // skip over comments

  if(buf[0] != 'P' || buf[1] != '1') {
    fclose(fpi);
    printf(" \n The input image is not pbm format! \n\n");
    return -1;
  }   

  do   {
       fgets(buf , 256 , fpi) ;
   } while(buf [0 ] == '#') ;  // skip over comments

  sscanf(buf , "%d %d" , &width , &height) ; // 画像サイズを読み込む

  int img[height][width];

  for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      fscanf(fpi, "%d", &img[y][x]);
    }
  }   //    画像中(x,y)の位置にある画素をimg[y][x]で参照できる。

  fclose(fpi); 

  //画像データに対する処理, 0,1 逆転

 for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      if(img[y][x] == 1) 
	img[y][x] = 0; 
      else  
	img[y][x] = 1; 
    }
 }


 printf("Please input the file name of the output pbm image:  "); 

 //出力画像名を読み込む
  scanf("%s", fnameout); 


 //画像データをファイル fnameout に出力する

  fpo=fopen(fnameout, "w");

  if(fpo==NULL)
    printf("Reading file open error ! %s\n", fnameout); 

  fprintf(fpo, "P1\n") ;
  fprintf(fpo, "# %s\n", fnameout);
  fprintf(fpo, "%d %d \n", width, height);

  int count=0; 

  for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      fprintf(fpo, "%1d ", img[y][x]);
      count++;
      if(count%28 == 0){
	fprintf(fpo, "\n");  //1行28個画素
      }
    }
  }
  fclose(fpo) ;  // 出力画像をクローズする

  printf("\n Proccessing finished. \n Output image file is: %s\n\n", fnameout); 

}
