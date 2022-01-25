
rm -f ./src/config.json


echo "{\"url\": \"http://"$(ip -4 address show dev eth0 | grep inet | awk '{ print $2 }' | sed 's|/.*$||')":8080/api\"}" >> ./src/config.json
# echo $(ip -4 address show dev eth0 | grep inet | awk '{ print $2 }' | sed 's|/.*$||') >> ./src/config.json