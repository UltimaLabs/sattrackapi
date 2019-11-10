#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters."
    echo "Usage: ${0} path num_jars_to_keep"
    exit 1
fi

path=$1
keep_jars=$2

if [ "$keep_jars" -le 0 ]; then
    echo "num_jars_to_keep must be greater than or equal to 1."
    exit 1
fi

total_files=$(find ${path} -maxdepth 1 -name '*.jar' -type f | wc -l)
num_to_remove=`expr ${total_files} - ${keep_jars}`

if [ "$num_to_remove" -le 0 ]; then
    exit 0
fi

find ${path} -maxdepth 1 -name '*.jar' -type f | sort -V | head -n $num_to_remove | xargs rm
