FROM python:3.7.2-alpine3.7

ENV user django

RUN apk update && apk add --no-cache jpeg-dev zlib-dev freetype-dev lcms2-dev openjpeg-dev tiff-dev tk-dev tcl-dev gcc python3-dev linux-headers libc-dev musl-dev mariadb-dev && adduser $user -D # for Pillow

# install require
COPY dreamsign/requirements.txt /tmp/requirements.txt
RUN pip install -r /tmp/requirements.txt

# copy app dir
COPY dreamsign/ /app
WORKDIR /app

# create flag file
RUN echo "DH{[***DELETED***]}" > /flag

# run cmd
CMD ["/app/docker-entrypoint.sh"]
