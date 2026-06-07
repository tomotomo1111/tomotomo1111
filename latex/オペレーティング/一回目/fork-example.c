#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>


int main(void)
{

  int itmp = 0;

  pid_t pid;

  pid = fork();

  if ( pid == 0 ) {
    int n;
    for ( n=0; n < 5; n++ ) {
      itmp +=100;
      printf("child  process: %d (pid %d), itmp: %d\n", n, getpid(), itmp);
      fflush(stdout);
      /*      sleep(3); */
	}
    _exit(0);
  } else if ( pid > 0 ) {
      int n;
      itmp = 0;
      for ( n=0; n < 5; n++ ) {
	printf("parent process: %d (pid %d), itmp: %d\n", n, getpid(), itmp);
	fflush(stdout);
	/*	sleep(3); */
      }
  } else {
      fprintf(stderr, "could not fork...\n");
      exit(1);
    }
  return 0;
}


