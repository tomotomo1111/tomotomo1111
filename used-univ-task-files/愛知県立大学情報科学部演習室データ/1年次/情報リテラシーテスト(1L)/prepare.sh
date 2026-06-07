#!/bin/bash
if [ $# -gt 0 ] ; then 
  uid=$1
else
  uid=$USER
fi
if [[ ! $uid =~ ^i[ms][0-9]{6}$ ]] ; then
  echo $0 の後にisから始まるユーザ名を入力してください 
  exit
fi
wget https://www.ist.aichi-pu.ac.jp/~ohta/literacy/exam/3409vfuhdiavhgha/$uid.zip
unzip $uid.zip
platex -shell-escape exam-2022
platex -shell-escape exam-2022
dvipdfmx -o exam-2022-org.pdf exam-2022
