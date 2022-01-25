function unresc {
        if [[ $comm == *[';''&''/']* ]]
        then 
                return 0
        fi

        return 1
}
while :
do
        echo "Enter the command you want to execute:"
        read comm
        if unresc "$comm" 
        then
                echo 'What are you trying to do???'
        else
                eval $comm
		echo "Did you get it??"
        fi
done 