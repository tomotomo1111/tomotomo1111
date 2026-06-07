#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int data;
    struct node *next;
    struct node *prev;
} node;

int insert_node(int data, node **head, node **tail);
void delete_node(node *np, node **head, node **tail);
void show_list(node *head);
int sort_list(node **head, node **tail);
void free_list(node *head);

int main(void) {
    node *head = NULL, *tail = NULL;
    int num;

    scanf("%d", &num);
    if (insert_node(num, &head, &tail) == 0) return 0;
    while (1) {
        scanf("%d", &num);
        if (num == 0) break;
        if (insert_node(num, &head, &tail) == 0) return 0;
    }

    sort_list(&head, &tail);
    show_list(head);
    free_list(head);

    return 1;
}

int insert_node(int data, node **head, node **tail) {
    node *new_node = (node *)malloc(sizeof(node));
    if (new_node == NULL) return 0;
    new_node->data = data;
    new_node->next = NULL;
    new_node->prev = NULL;
    if (*head == NULL) {
        *head = new_node;
        *tail = new_node;
    } else {
        (*tail)->next = new_node;
        new_node->prev = *tail;
        *tail = new_node;
    }

    return 1;
}

void delete_node(node *np, node **head, node **tail) {
    if (np == NULL) return;

    if (np == *head) {
        *head = np->next;
        if (*head != NULL) (*head)->prev = NULL;
    } else {
        np->prev->next = np->next;
    }

    if (np == *tail) {
        *tail = np->prev;
        if (*tail != NULL) (*tail)->next = NULL;
    } else {
        np->next->prev = np->prev;
    }

    free(np);
}

void show_list(node *head) {
    node *np = head;
    while (np != NULL) {
        printf("%d ", np->data);
        np = np->next;
    }
    printf("\n");
}

int sort_list(node **head, node **tail) {
    if (*head == NULL) return 1;

    node *sorted_head = NULL, *sorted_tail = NULL;

    while (*head != NULL) {
        node *np = *head;
        *head = (*head)->next;

        if (sorted_head == NULL) {
            np->next = NULL;
            np->prev = NULL;
            sorted_head = np;
            sorted_tail = np;
        } else {
            node *insert_pos = sorted_head;
            while (insert_pos != NULL && insert_pos->data < np->data) {
                insert_pos = insert_pos->next;
            }

            if (insert_pos == NULL) {
                sorted_tail->next = np;
                np->prev = sorted_tail;
                np->next = NULL;
                sorted_tail = np;
            } else if (insert_pos == sorted_head) {
                np->next = sorted_head;
                np->prev = NULL;
                sorted_head->prev = np;
                sorted_head = np;
            } else {
                np->next = insert_pos;
                np->prev = insert_pos->prev;
                insert_pos->prev->next = np;
                insert_pos->prev = np;
            }
        }
    }

    *head = sorted_head;
    *tail = sorted_tail;

    return 1;
}

void free_list(node *head) {
    node *np = head;
    while (np != NULL) {
        node *next = np->next;
        free(np);
        np = next;
    }
}