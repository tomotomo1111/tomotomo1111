#!/bin/bash

if [ -z $1 ]; then
  echo "引数が不足しています。c ファイルの名前を指定してください。"
  exit 1
fi

cFileName="$1"
FileName="${cFileName%.c}"

counter=0
while [ $counter -lt 1000 ]; do
  platex "$cFileName"
  gcc "$cFileName" -o "$FileName"
  ./"$FileName"
  sleep 4
  ((counter++))
done

exit 0
