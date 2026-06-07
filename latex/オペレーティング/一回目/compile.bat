@echo off
gcc thread-semaphore.c -lpthread -o with-sema
gcc thread-without-semaphore.c -lpthread -o without-sema
