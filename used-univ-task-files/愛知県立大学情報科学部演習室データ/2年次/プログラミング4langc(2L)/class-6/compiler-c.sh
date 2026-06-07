#!/bin/bash

for file in *.c
do
    base="${file%.c}"
    gcc "$file" -lm -o "$base" && ./"$base"
done