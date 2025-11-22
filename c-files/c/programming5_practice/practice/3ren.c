#include <stdio.h>
#include <string.h>
#define DOTANDRIGHTNUMBERS 7

int main() {
    char filename[100];
    scanf("%99s", filename);

    FILE *fp;
    if ((fp = fopen(filename, "rb")) == NULL) return 0;

    char data[256];
    if (fread(data, sizeof(char), 255, fp) != 255) return 0;
    
    char *time, *a, *north, *n, *east, *e;
    time = strtok(data, ",");
    a = strtok(NULL, ",");
    north = strtok(NULL, ",");
    n = strtok(NULL, ",");
    east = strtok(NULL, ",");
    e = strtok(NULL, ",");

    char hour[3] = {0};
    char min[3] = {0};
    char sec[3] = {0};
    char timeleft[4] = {0};
    strncpy(hour, time + 0, 2);
    strncpy(min, time + 2, 2);
    strncpy(sec, time + 4, 2);
    strncpy(timeleft, time + 7, 3);

    int north_len = strlen(north);
    int east_len = strlen(east);
    int hokui_d_len = north_len - DOTANDRIGHTNUMBERS;
    int toukei_d_len = east_len - DOTANDRIGHTNUMBERS;
    char hokui_deg[4] = {0};
    char toukei_deg[4] = {0};
    char hokui_right[DOTANDRIGHTNUMBERS + 1] = {0};
    char toukei_right[DOTANDRIGHTNUMBERS + 1] = {0};

    strncpy(hokui_deg, north + 0, hokui_d_len);
    strncpy(toukei_deg, east + 0, toukei_d_len);
    strncpy(hokui_right, north + hokui_d_len, DOTANDRIGHTNUMBERS);
    strncpy(toukei_right, east + toukei_d_len, DOTANDRIGHTNUMBERS);
    printf("%s��%s��%s�b%s �k��%s�x%s���o%s�x%s", hour, min, sec, timeleft, hokui_deg, hokui_right, toukei_deg, toukei_right);
    
    fclose(fp);
    return 1;
}