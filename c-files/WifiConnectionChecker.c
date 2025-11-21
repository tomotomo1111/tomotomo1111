#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <windows.h>

// Wi-Fi接続がインターネットに接続できているかを確認する関数
int check_internet_connection() {
    int result = system("ping -n 1 www.google.com > nul");
    if (result == 0) {
        return 1; // インターネット接続が成功
    } else {
        return 0; // インターネット接続が失敗
    }
}

// Wi-Fi接続が有効かどうかを確認する関数
int check_wifi_connection() {
    FILE *fp;
    char buffer[512];
    int connected = 0;
    fp = popen("netsh wlan show interfaces", "r");
    if (fp == NULL) {
        printf("Error: Failed to execute command\n");
        return -1;
    }
    while (fgets(buffer, sizeof(buffer), fp) != NULL) {
        if ((strstr(buffer, "State") != NULL && strstr(buffer, "connected") != NULL)
            || (strstr(buffer, "状態") != NULL && strstr(buffer, "接続されました") != NULL)) {
            connected = 1;
            break;
        }
        printf("%s", buffer);
    }
    pclose(fp);
    return connected;
}

int main() {
    int wifi_status, internet_status;
    while (1) {
        wifi_status = check_wifi_connection();
        if (wifi_status == 0) {
            printf("Wi-Fi is not connected.\n");
        } else {
            internet_status = check_internet_connection();
            if (internet_status == 0) {
                printf("Wi-Fi is connected but unable to access the internet.\n");
            } else {
                printf("Wi-Fi is connected and internet access is available.\n");
            }
        }
        Sleep(10000); // 10秒ごとにチェック
    }
    return 0;
}
