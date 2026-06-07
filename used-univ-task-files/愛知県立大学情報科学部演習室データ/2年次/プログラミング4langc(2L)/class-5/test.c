#include <stdio.h>

int main (void) {
    int x;
    scanf("%d", &x);
    switch (x > 0) {
        case 0 :
            switch (x < 0) {
                case 0 :
                    puts("x is zero");
                    break;
                case 1 :
                    puts("x is a negative number");
                    break;
            }
            break;
        case 1:
            puts("x is a positive number");
            break;
    }
}