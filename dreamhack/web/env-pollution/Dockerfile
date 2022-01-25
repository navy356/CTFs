FROM node:latest
COPY . /app
WORKDIR /app

ENV MYSQL_USER REDACTED
ENV MYSQL_PASSWORD REDACTED
ENV SECRET REDACTED
ENV PORT 27892

RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install mariadb-server mariadb-client -y

RUN npm install express cookie-parser fs multer mysql jsonwebtoken child_process --save

RUN chmod u+r+x /app/entrypoint.sh
ENTRYPOINT /app/entrypoint.sh

EXPOSE 27892