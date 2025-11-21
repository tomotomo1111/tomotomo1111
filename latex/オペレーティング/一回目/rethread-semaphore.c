#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>

#include <semaphore.h>

#define IMAX 1000000

int itmp = 0;
sem_t sema;

void countA(int x){
  int i;

  for ( i=0; i<IMAX; i++ ){
    sem_wait(&sema);
    itmp++;
    sem_post(&sema);
  }
}

void countB(int x){
  int i;

  for ( i=0; i<IMAX; i++ ){
    sem_wait(&sema);
    itmp--;
    sem_post(&sema);
  }
}

int main(void)
{
  int sinit;
  
  pthread_t pt1;
  pthread_t pt2;

  sinit = sem_init( &sema, 0, 1 ); 

  pthread_create(&pt1, NULL, (void*)countA,(void*)1);
  pthread_create(&pt2, NULL, (void*)countB,(void*)2);

  printf("in main()\n");
  pthread_join(pt1, NULL);
  pthread_join(pt2, NULL);
  printf("after pthread_join itmp: %d\n", itmp);


}


