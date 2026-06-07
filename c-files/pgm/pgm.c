#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define PHOTOGRAPH_WIDTH 1280
#define PHOTOGRAPH_HEIGHT 720
#define BRIGHTNESS_MAX 256
#define CX 640
#define CY 360
#define RING_DISTANCE_FROM_CENTER 200
#define RING_WIDTH 60

int cal_distance_between_double_point(int x, int y, int cx, int cy);
int check_isin_the_ring(int pixel_distance_from_center);


int main(void) {

    FILE *f;
    if ((f = fopen("m.pgm", "w")) == NULL){
        fclose(f);
        return -1;
    }

    fprintf(f, "%s\n", "P2");
    fprintf(f, "%s\n", "# Shows the word \"FEEP\" (example from Netpbm man page on PGM)");
    fprintf(f, "%d %d\n", PHOTOGRAPH_WIDTH, PHOTOGRAPH_HEIGHT);
    fprintf(f, "%d\n", BRIGHTNESS_MAX);
    
    int i, j;
    for (i = 0; i < PHOTOGRAPH_HEIGHT; i++) {
        for (j = 0; j < PHOTOGRAPH_WIDTH; j++) {
            fprintf(f, "%d ",
                check_isin_the_ring(
                    cal_distance_between_double_point(j, i, CX, CY)
                ) ? (i + j) % BRIGHTNESS_MAX : 0
            );
        }
        fputc('\n', f);
    }

    fclose(f);
    return 1;
}

int cal_distance_between_double_point(int x, int y, int cx, int cy) {

    int x_abs = abs(x - CX);
    int y_abs = abs(y - CY);
    return x_abs * x_abs + y_abs * y_abs;
}

int check_isin_the_ring(int pixel_distance_from_center) {
    if (pixel_distance_from_center >= RING_DISTANCE_FROM_CENTER - RING_WIDTH
        && pixel_distance_from_center <= RING_DISTANCE_FROM_CENTER + RING_WIDTH
    ) {
        return 1;
    } else {
        return 0;
    }
}