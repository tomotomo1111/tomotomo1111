#include <stdio.h>
#include <stdlib.h>

typedef struct node{
    int data;
    struct node *next;
    struct node *prev;
} node;

int insert_node(int value, node **head, node **tail) {
    node *np;
    if ((np = (node *)malloc(sizeof(node))) == NULL) return 0;
    np->data = value;
    np->next = *head;
    np->prev = NULL;
    if (*head != NULL) {
        (*head)->prev = np;
    } else {
        *tail = np;
    }
    *head = np;
    return 1;
}

void show_list(node *head) {
    node *np = head;
    while (np != NULL) {
        printf("%d ", np->data);
        np = np->next;
    }
    putchar('\n');
}

void free_list(node *head) {
    node *np = head;
    node *tmp;
    while (np != NULL) {
        tmp = np;
        np = np->next;
        free(tmp);
    }
}

node *split(node *head) {
    node *fast = head;
    node *slow = head;
    while (fast->next && fast->next->next ) {
        fast = fast->next;
        fast = fast->next;
        slow = slow->next;
    }
    node *tmp = slow->next;
    slow->next = NULL;
    return tmp;
}

node *merge(node *first, node *second) {
    if (!first) return second;
    if (!second) return first;
    if (first->data < second->data) {
        first->next = merge(first->next, second);
        first->next->prev = first;
        first->prev = NULL;
        return first;
    } else {
        second->next = merge(first, second->next);
        second->next->prev = second;
        second->prev = NULL;
        return second;
    }
}

node *merge_sort(node *head) {
    if (!head || !head->next) return head;
    node *second = split(head);
    head = merge_sort(head);
    second = merge_sort(second);
    return merge(head, second);
}

int main() {
    node *head = NULL, *tail = NULL;
    int input;
    while (scanf("%d", &input) && input != 0) {
        if (insert_node(input, &head, &tail) == 0) return 0;
    }
    head = merge_sort(head);
    show_list(head);
    free_list(head);
    return 1;
}