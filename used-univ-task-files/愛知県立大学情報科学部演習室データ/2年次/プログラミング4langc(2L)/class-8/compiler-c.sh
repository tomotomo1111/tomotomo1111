#!/bin/bash

for file in *.c
do
    base="${file%.c}"
    gcc "$file" -lm -o "$base" && ./c8-3
done