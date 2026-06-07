#!/bin/sh


echo "西暦でお年はおいくつですか"
read year
if [ $year -gt $2019 ];then
  echo "令和生まれですね" 
    elif [ $year -gt $1989 ];then
    echo "平成生まれですね"
      else  
      echo "昭和以下生まれですね"
fi

