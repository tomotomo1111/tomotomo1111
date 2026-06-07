#!/bin/bash
for n in `seq -f%04.0f 0 9999` ; do
  if a=`openssl enc -d -aes-256-cbc -base64 -in $1 -pass pass:$n 2> /dev/null` ; then
    b=`echo $n" "$a | strings | head -c160 | head -n1`
    echo $b
  fi
done
