#!/bin/bash

for file in *.tex
do
    base="${file%.tex}"
    platex "$file"
    platex "$file"
    dvipdfmx "$base.dvi"
done
