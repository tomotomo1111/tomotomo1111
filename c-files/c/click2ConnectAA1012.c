#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int connectLan(char *lanName, int lanNameLen);

int main(void) {
    char *lan = "AA101248387S";
    int a = -1;

    printf("show interfaces : %d",system("netsh wlan show interfaces"));
    a = connectLan(lan, strlen(lan));
    printf("a : %d\n", a);

    return a;
}

int connectLan(char *lanName, int lanNameLen) {
    if (lanNameLen <= 0) {
        return -1;
    }

    char command[100];
    sprintf(command, "netsh wlan connect \"%s\"", lanName);
    system(command);
    
    return 1;
}
