#!/bin/sh

if [ -f ~/list.txt ] 
  then echo "存在する"
  else touch ~/list.txt
fi
