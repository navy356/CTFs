#!/bin/bash

# start node
su redis -s /bin/sh -c "redis-server >/dev/null&"
sleep 3;
su node -s /bin/sh -c "cd node_api;node main.js &"

# start python
su pyapp -s /bin/sh -c "cd ssrf;python3 app.py &"

# start php
/usr/sbin/httpd

# sleep
sleep infinity