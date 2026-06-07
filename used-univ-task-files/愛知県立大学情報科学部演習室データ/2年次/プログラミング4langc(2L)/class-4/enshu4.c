/*
出力
        *
       **
      ***
     ****
    *****
   ******
  *******
 ********
*/

#include <stdio.h>

int main(void) {

    for(int i = 0; i < 8; i++) {
        for (int j = 8; j >= 0; j--) {
            if (j > i) {
                putchar(' ');
            } else {
                putchar('*');
            }
        }
        putchar('\n');
    }
}