#include <stdio.h>
#include <stdbool.h>

bool verification(char *string1, char *string2);

int main()
{
    char string1[100], string2[100];
    bool result;
    scanf("%s", string1);
    scanf("%s", string2);
    if(verification(string1,string2) == true)
        printf("2つの文字列は同じです");
    else
        printf("2つの文字列は異なります");
    return 0;
}

bool verification(char *string1, char *string2){
    bool ans = false;
    while ( *string1 == *string2) {
        if( *(string1 + 1) == '\0') ans = true;   
        string1++; string2++;
    }
    return ans;

    
}