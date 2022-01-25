#!/bin/bash -x

sess_id=$(curl http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi|egrep -o "\"sid\":\"(.{20})\""|awk -F":" '{print $2}'|tr -d "\"")

response=$(curl 'http://web4.affinityctf.com/socket.io/?EIO=4&transport=polling&t=NNJMtCi&sid='$sess_id \
  -H 'Connection: keep-alive' \
  -H 'Accept: */*' \
  -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36' \
  -H 'Content-type: text/plain;charset=UTF-8' \
  -H 'Origin: http://web4.affinityctf.com' \
  -H 'Referer: http://web4.affinityctf.com/' \
  -H 'Accept-Language: en-GB,en-US;q=0.9,en;q=0.8' \
  --data-binary '40' \
  --compressed \
  --insecure)

response2=$(curl -d "3" -X POST http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi\&sid=$sess_id)

response3=$(curl http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi\&sid=$sess_id)

if [[ $(echo $response3|grep -o "data") == "" ]]
then
	sess_id=$(echo $response3|egrep -o "\"sid\":\"(.{20})\""|awk -F":" '{print $2}'|tr -d "\"")

	response4=$(curl -d "3" -X POST http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi\&sid=$sess_id)

	response6=$(websocat ws://web4.affinityctf.com/socket.io/?EIO=4&transport=websocket&sid=$sid   -H 'Pragma: no-cache'   -H 'Origin: http://web4.affinityctf.com'   -H 'Accept-Language: en-GB,en-US;q=0.9,en;q=0.8'   -H 'Sec-WebSocket-Key: 00R1c7m547XNQbT++FeMEQ=='   -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36'   -H 'Upgrade: websocket'   -H 'Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits'   -H 'Cache-Control: no-cache'   -H 'Connection: Upgrade'   -H 'Sec-WebSocket-Version: 13')

	sess_id=$(curl http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi|egrep -o "\"sid\":\"(.{20})\""|awk -F":" '{print $2}'|tr -d "\"")

	response2=$(curl -d "3" -X POST http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi\&sid=$sess_id)

	response3=$(curl http://web4.affinityctf.com/socket.io/\?EIO=4\&transport=polling\&t=NNJMtCi\&sid=$sess_id)
fi

#$data=$(echo $response3|egrep -o "\"data:image/png;base64,.*\""|awk -F"," '{print $2}'|tr -d "\"")

#echo $data|base64 -d>~/Desktop/Affinity/image.png


