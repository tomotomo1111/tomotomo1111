#!/bin/bash

if [ -z $1 ]; then
  echo "引数が不足しています。Tex ファイルの名前を指定してください。"
  exit 1
fi

texFileName="$1"
dviFileName="${texFileName%.tex}.dvi"
pdfFileName="${texFileName%.tex}.pdf"

counter=0
while [ $counter -lt 1000 ]; do
  platex "$texFileName"
  platex "$texFileName"
  dvipdfmx "$dviFileName"
  pdfopen --file "$pdfFileName"
  sleep 60
  pdfclose --file "$pdfFileName"
  ((counter++))
done

exit 0
