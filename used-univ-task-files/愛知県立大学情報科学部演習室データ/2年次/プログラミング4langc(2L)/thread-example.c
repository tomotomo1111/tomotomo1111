#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>

int itmp = 0;


void count(int x){
  int i;
  for ( i=0; i< 5; i++ ){
    itmp +=50;
    printf("in count(%d): %d, itmp: %d\n",x, i, itmp);
    fflush(stdout);
    /*    sleep(1); */
  }
}

int main(void)
{

  pthread_t pt1;
  pthread_t pt2;

  pthread_create(&pt1, NULL, (void*)count,(void*)1);
  pthread_create(&pt2, NULL, (void*)count,(void*)2);

  printf("in main()\n");
  pthread_join(pt1, NULL);
  pthread_join(pt2, NULL);


}


