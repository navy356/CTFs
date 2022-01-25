FROM node:dubnium-alpine

RUN set -ex \
    && addgroup -S -g 1111 redis && adduser -S -G redis -u 1111 redis \
    && addgroup -S -g 2222 pyapp && adduser -S -G pyapp -u 2222 pyapp \
    && addgroup -S -g 3333 www-data && adduser -S -G www-data -u 3333 www-data \
    && addgroup -S -g 4444 phpythonode && adduser -S -G phpythonode -u 4444 phpythonode \
    && apk update \
    && apk add --no-cache bash redis python3 py3-setuptools apache2 php7 php7-apache2 gcc musl-dev

ADD ./deploy /app


# node
WORKDIR /app/node_api
RUN npm install

# python
WORKDIR /app/ssrf
RUN pip3 install flask requests

# php
WORKDIR /app/php-1
ENV APACHE_RUN_USER=www-data \
    APACHE_RUN_GROUP=www-data \
    APACHE_LOG_DIR=/var/log/apache2
RUN rm /var/www/localhost/htdocs/index.html \
    && mv src/* /var/www/localhost/htdocs \
    && mkdir /var/www/localhost/uploads \
    && mv flag.php hello.json /var/www/localhost/uploads \
    && chown -R www-data:www-data /var/www/localhost/uploads

# FLAG

WORKDIR /
RUN mv /app/flag_phpythonode.txt / \
    && echo 'int main(){setreuid(geteuid(), geteuid());setregid(getegid(), getegid());system("id;cat /flag_phpythonode.txt");}' > /tmp/a.c \
    && gcc /tmp/a.c -o /readflag \
    && chown phpythonode:phpythonode /flag_phpythonode.txt /readflag && chmod 4777 /readflag && chmod 400 /flag_phpythonode.txt

# Setting docker-entrypoint.sh
WORKDIR /app
RUN chmod +x docker-entrypoint.sh \
    && mv docker-entrypoint.sh /usr/local/bin/

ENTRYPOINT ["docker-entrypoint.sh"]
