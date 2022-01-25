#!/bin/sh

directory=/files
mkdir $directory/convert


converttask () {
    original_prefix="original_"
    srcfile=$1
    directory=$2
    reldir=$3
    file=$4
    base=${file%.*}
    tmpsrcfile="$directory$reldir$base.tmp"




    cp "$srcfile" "$directory$reldir$original_prefix$file"
    convert -resize 50% "$srcfile" "$tmpsrcfile" 
    mv "$tmpsrcfile" "$directory$reldir$base.png" 
    rm "$srcfile"
}


inotifywait -r -m $directory/convert -e close_write |
    while read dir action file; do

        ts=$(date +%s%N)
        reldir=${dir#$directory/convert}
        # Only for files
        srcfile=$directory/convert$reldir$file
        # echo "Current File: $srcfile with $action"
        if   [ -d "${srcfile}" ]
            then :
            #  Don't do anything if it is just a directory
        elif [ -f "${srcfile}" ]; then
            if [ ! -d "$directory$reldir" ]; then
                mkdir "$directory$reldir"
            fi
                converttask $srcfile $directory $reldir $file &
        fi
        diff=$((($(date +%s%N) - $ts)/1000000))
        if [ "$diff" -gt "100" ]; then
            echo "Took $diff"
        fi
    done
