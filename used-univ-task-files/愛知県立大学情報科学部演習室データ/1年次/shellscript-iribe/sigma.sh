#!/bin/sh
echo "最大は?"
read max
echo "最小は?"
read min
sigma=0;for n in `seq $min $max`;
       do sigma=$(($sigma+$n));
       done;echo "合計は$sigma"
