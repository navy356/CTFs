#!/bin/bash

page=$(curl -vs https://doitfast.wearemist.in/ 2>&1 | less)
cookie=$(echo $page|egrep -o "set-cookie: PHPSESSID=.*;"|awk -F= '{print $2}'|tr -d ";")
cfduid=$(echo $page|egrep -o "set-cookie: __cfduid=.*;"|awk -F= '{print $2}'|awk -F' ' '{print $1}'|tr -d ";")
flag=$(echo $page|egrep -o "get-flag: .{24}"| awk -F" " '{print $2}')
decoded_flag=$(echo $flag|base64 -d)

curl 'https://doitfast.wearemist.in/index.php' \
  -H 'authority: doitfast.wearemist.in' \
  -H 'cache-control: max-age=0' \
  -H 'upgrade-insecure-requests: 1' \
  -H 'origin: https://doitfast.wearemist.in' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36' \
  -H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9' \
  -H 'sec-fetch-site: same-origin' \
  -H 'sec-fetch-mode: navigate' \
  -H 'sec-fetch-user: ?1' \
  -H 'sec-fetch-dest: document' \
  -H 'referer: https://doitfast.wearemist.in/' \
  -H 'accept-language: en-GB,en-US;q=0.9,en;q=0.8' \
  -H 'cookie: __cfduid='$cfduid'; PHPSESSID='$cookie \
  --data-raw 'QCTF='$decoded_flag \
  --compressed
