#!/bin/sh
#数値を変えて動作確認
num=10
#＝の前後に半角スペースを入れない
echo "numの値は"
if [ "$num" -eq 50 ];then
  echo "50である"
elif [ "$num" -gt 50 ];then
  echo "50より大きい"
else 
  echo "50未満"
fi
