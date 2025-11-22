#include <stdio.h>
#include <string.h>
#define NHUN_LEN 7
#define EHUN_LEN 7

int main() {
    char filename[256];
    if (scanf("%99s", filename) != 1) return 0;
    FILE *fp;
    if ((fp = fopen(filename, "rb")) == NULL) return 0;
    char data[256];
    if (fread(data, sizeof(char), 256, fp) == 0) return 0;
    
    char *time, *north, *east, *a, *n, *e;
    if ((time = strtok(data, ",")) == NULL) return 0;
    char hour[3] = {0};
    char min[3] = {0};
    char sec[3] = {0};
    char timeleft[4] = {0};
    strncpy(hour, time, 2);
    strncpy(min, time + 2, 2);
    strncpy(sec, time + 4, 2);
    strncpy(timeleft, time + 7, 3);
    printf("%sŽž%s•ª%s•b%s ", hour, min, sec, timeleft);
    if ((a = strtok(NULL, ",")) == NULL) return 0;

    if ((north = strtok(NULL, ",")) == NULL) return 0;
    int n_len = strlen(north);
    char hokui[4] = {0};
    char nhun[NHUN_LEN + 1] = {0};
    strncpy(hokui, north, n_len - NHUN_LEN);
    strncpy(nhun, north + n_len - NHUN_LEN, NHUN_LEN);
    printf("–kˆÜ%s“x%s•ª", hokui, nhun);
    if ((n = strtok(NULL, ",")) == NULL) return 0;

    if ((east = strtok(NULL, ",")) == NULL) return 0;
    int e_len = strlen(east);
    char toukei[4] = {0};
    char ehun[EHUN_LEN + 1] = {0};
    strncpy(toukei, east, e_len - EHUN_LEN);
    strncpy(ehun, east + e_len - EHUN_LEN, EHUN_LEN);
    printf("“ŒŒo%s“x%s•ª", toukei, ehun);
    if ((e = strtok(NULL, ",")) == NULL) return 0;
    return 1;
}