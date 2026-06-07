#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>
#include <semaphore.h>

#define N 5
#define THINKING 0
#define HUNGRY 1
#define EATING 2
#define TRUE 1

int state[N] = {EATING, EATING, EATING, EATING, EATING};
sem_t mutex;
sem_t s[N]={0};

void test(int i) {
    if (state[i] == HUNGRY && state[(i + N - 1) % N] != EATING && state[(i + N + 1) % N] != EATING) {
        state[i] = EATING;
        sem_post(&s[i]);
    }
}

void take_forks(int i) {
    sem_wait(&mutex);
    state[i] = HUNGRY;
    test(i);
    sem_post(&mutex);
    sem_wait(&s[i]);
}

void put_forks(int i) {
    sem_wait(&mutex);
    state[i] = THINKING;
    test((i + N - 1) % N);
    test((i + N + 1) % N);
    sem_post(&mutex);
}

void philosopher(int i) {
    while(TRUE) {
        take_forks(i);
        printf("Philosopher %d is eating.\n", i);
        put_forks(i);
    }
}

int main(void) {
    int sinit, i;
    pthread_t philosophers[N];
    sem_init(&mutex, 0, 1);

    for (i = 0; i < N; i++) {
        sinit = sem_init(&s[i], 0, 0);
    }

    for (i = 0; i < N; i++) {
        pthread_create(&philosophers[i], NULL, (void*)philosopher, (void*)(intptr_t)i);
    }

    for (i = 0; i < N; i++) {
        pthread_join(philosophers[i], NULL);
    }

    return 0;
}