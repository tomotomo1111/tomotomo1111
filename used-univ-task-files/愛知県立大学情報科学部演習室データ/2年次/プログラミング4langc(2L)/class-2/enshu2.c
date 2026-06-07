/*  実行結果

     AA     
            
    AAAA    
            
   AA  AA   
            
  AA    AA  
            
 AAAAAAAAAA 
            
AA        AA
            
*/

#include <stdio.h>

#define ROW 13
int main(void) {

    int i;

    for(i = 0; i<ROW; i++) {
        switch(i) {
            case 1  : printf("     AA     \n"); break;
            case 3  : printf("    AAAA    \n"); break;
            case 5  : printf("   AA  AA   \n"); break;
            case 7  : printf("  AA    AA  \n"); break;
            case 9  : printf(" AAAAAAAAAA \n"); break;
            case 11 : printf("AA        AA\n"); break;
            default : printf("            \n"); break;
        }
    }
}