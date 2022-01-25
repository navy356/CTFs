#!/bin/bash

file_name=$(ls -1A|awk 'NR==1{print $1}')
echo "$file_name"
while true
do
i="file_emp"
file_type=$(file "$file_name")
tar_type=$(echo "$file_type"|grep -i "tar")
gzip_type=$(echo "$file_type"|grep -i "gzip") 
zip_type=$(echo "$file_type"|grep -i "Zip")
xz_type=$(echo "$file_type"|grep -i "xz")
rar_type=$(echo "$file_type"|grep -i "rar")
if [[ $tar_type != '' ]]
then
	echo "Tar"
	new_file_name=$(tar xvf $file_name --to-command=echo)
	tar xvf $file_name
	rm -f $file_name
	file_name="$new_file_name"
elif [[ $gzip_type != '' ]]
then
	echo "Gzip"
	new_file_name=$(gunzip -lN $file_name| tr " " "\n"|awk 'END{print}')
	7z x $file_name -so > $new_file_name
	code=$(echo $?)
	if [[ $code != '0' ]]
	then
		break
	fi
	rm -f $file_name
	file_name="$new_file_name"
elif [[ $zip_type != '' ]]
then
	echo "Zip"
	protect=$(7z l -slt $file_name | grep -o ZipCrypto)
	if [[ $protect != '' ]]
	then
		echo "protected"
		~/bin/john/run/zip2john $file_name >testf
		~/bin/john/run/john --wordlist=xato-net-10-million-passwords-100.txt testf
		echo "Here"
		password=$(~/bin/john/run/john testf --show|tr ":" "\n"|awk 'NR==2{print $1}')
		new_file_name=$(~/bin/john/run/john testf --show|tr ":" "\n"|awk 'NR==3{print $1}')
		echo "password=$password"
		rm -f testf
		7z x $file_name -p$password
		rm -f $file_name
		file_name="$new_file_name"
	else
		new_file_name="$i"
		7z x $file_name -so > $i
		echo "unprotected"
	fi
elif [[ $xz_type != '' ]]
then
	echo "XZ"
	break
	
elif [[ $rar_type != '' ]]
then
	echo "rar"
	break
else
	echo "new"
	break
fi
done
