#include <stdin.h>
#include <stdlib.h>

int main(void) {

    int s = 1;
    int n = 7;
    int x = 11;

    char name[7];
    char command[] = "mv ";
    char identifer[] = ".png";
    char rename[100], out[100];

    for (int i = 0; i < 7; i++) name[i] = 0 + '0';
    name[7] = '\0';
    for (int j = 0; j < n; j++) {
        
        sprintf(rename, name, identifer);
        sprintf(out, command, rename);
        system(out);
    }
}