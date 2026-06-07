#include <stdio.h>
#include <string.h>

int main() {
    char *filename = "fileB.dat";

    FILE *fp;
    if ((fp = fopen(filename, "wb")) == NULL) return 0;

    char *data = "3 9 -3 7";
    if (fwrite(data, sizeof(char), 255, fp) != 1) return 0;
    fclose(fp);
    return 1;
}