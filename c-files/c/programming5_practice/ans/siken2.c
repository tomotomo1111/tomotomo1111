#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int data;
    node *next;
    node *prev;
} node;

int insert(int data, node **head, node **tail) {
    node *node;
    if ((node = (struct node*) malloc(sizeof(struct node))) == NULL) return 0;
    node->data = data;
    node->next = *tail;
    node->prev = *head;
    *head = node;
    *tail = node;

    return 1;
}

void show_list( node *np ){
    while( np != NULL ){
        printf( "%d ", np->data );
        np = np->next;
    }
    printf( "\n" );
    return;
}

void delete_node( node *np, node **head, node **tail ) {
    if (np == *head) {
        *head = np->next;
    } else {
        np->prev->next = np->next;
    }
    if (np == *tail){
        *tail = np->prev;
    } else {
        np->next->prev = np->prev;
    }
    free(np);
}


int main() {
    node *head = NULL; node *tail = NULL;
    int data;
    if (insert(data, &head, &tail) == 0) return 0; 
    do {
        scanf("%d", &data);
        if (data == 0) break;
        if (insert(data, &head, &(head->next)) == 0) return 0; 
    } while (1);
    return 1;
}