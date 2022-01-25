FROM node:dubnium-alpine

RUN mkdir /app

ADD ./docker-entrypoint.sh /app

WORKDIR /app

RUN apk add --no-cache musl-dev gcc \
    && npm install -g alasql@0.6.0 && sed -i 's/127.0.0.1/0.0.0.0/g' /usr/local/lib/node_modules/alasql/bin/alaserver.js\
    && mv docker-entrypoint.sh /usr/local/bin/\
    && gcc /app/flag.c -o /flag \
    && chmod 111 /flag && rm /app/flag.c

ENTRYPOINT ["sh", "/usr/local/bin/docker-entrypoint.sh"]