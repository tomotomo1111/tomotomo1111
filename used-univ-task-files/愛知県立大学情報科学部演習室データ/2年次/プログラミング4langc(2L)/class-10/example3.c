#include <stdio.h>
#define MAX 5
extern void rev_intary(int[], int);

int main(void) {
    int input[5] = {1, 2, 3, 4, 5};
    rev_intary(input, MAX);
    printf("{");
    for(int i = 0; i < MAX; i++) printf(" %d", input[i]); printf("}\n");
}