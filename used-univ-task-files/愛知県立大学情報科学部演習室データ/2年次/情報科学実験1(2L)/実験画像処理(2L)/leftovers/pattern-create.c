// ２値画像を生成する
// 

#include <stdio.h>
#include <string.h>
#include <math.h>

int main(void){
  FILE *fpo=NULL;  // 出力画像

  char buf[258];
  int width, height; // 画像の横長と縦長
  int maxvalue;  // 画像の最大輝度値
  int i, x, y;
  char fnameout[]="zukei.pbm"; 


  fpo=fopen(fnameout, "w");

  height = 158; 
  width = 158;  

  int img[height][width];

  if(fpo==NULL)
    printf("Reading file open error ! %s\n", fnameout); 

  fprintf(fpo, "P1\n") ;
  fprintf(fpo, "# %s\n", fnameout);
  fprintf(fpo, "%d %d \n", width, height);

  int count=0; 

  for(y=0; y<height; y++) {
    for(x=0; x<width; x++) {
      img[y][x] = 0; 
      if((20<=y)&&(y<=60)&&(20<=x)&&(x<=60))
       	img[y][x] = 1;
      //      if((50<=y)&&(y<=120)&&(130<=x)&&(x<=140))
      //	img[y][x] = 1;
      if((1.5*x-100<=y)&&(y<=80)&&(80<=x)&&(x<=120))
      	img[y][x] = 1; 
      if(((y-110)*(y-110) + (x-80)*(x-80)) <= 400)  
	img[y][x] = 1; 
      //     if(((y-30)*(y-30) + (x-130)*(x-130)) <= 144)
      //	img[y][x] = 1; 
      fprintf(fpo, "%1d ", img[y][x]);
      count++;
      if(count%28 == 0){
	fprintf(fpo, "\n");  //1行28個画素
      }
    }
  }
  fclose(fpo) ;  // 出力画像をクローズする
}
