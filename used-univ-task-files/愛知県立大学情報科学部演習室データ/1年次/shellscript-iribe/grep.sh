#!/bin/sh
if grep $1 $2 > /dev/null 2>&1;then
  echo "Yes"
else 
  echo "No"
fi
