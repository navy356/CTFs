#!/bin/bash
figlet "BASH JAIL x Fword"
echo "Welcome! Kudos to Anis_Boss Senpai"
function a(){
/usr/bin/env
}
export -f a
function calculSlash(){
	echo $1|grep -o "/"|wc -l
}
function calculPoint(){
	echo $1|grep -o "."|wc -l
}
function calculA(){
        echo $1|grep -o "a"|wc -l
}

while true;do
read -p ">> " input ;
if echo -n "$input"| grep -v -E "^(\.|\/| |\?|a)*$" ;then
        echo "No No not that easy "
else
	pts=$(calculPoint $input)
	slash=$(calculSlash $input)
	nbA=$(calculA $input)
	if [[ $pts -gt 2 || $slash -gt 1 || $nbA -gt 1 ]];then
		echo "That's Too much"
	else
		eval "$input"
	fi
fi
done
