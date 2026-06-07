#include <stdio.h>
#include <string.h>

int main() {
    char *filename = "fileA.dat";

    FILE *fp;
    if ((fp = fopen(filename, "wb")) == NULL) return 0;

    char *data = "085120.307,A,3541.1493,N,13945.3994,E";
    if (fwrite(data, sizeof(char), 255, fp) != 1) return 0;
    return 1;
}