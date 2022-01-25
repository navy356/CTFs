FROM ubuntu:16.04

ENV WDIR /lazy_judge
ENV RDIR /lazy_judge
COPY ./lazy_judge $WDIR/

RUN sed -i "s/http:\/\/archive.ubuntu.com/http:\/\/kr.archive.ubuntu.com/g" /etc/apt/sources.list
RUN apt-get update
RUN apt-get -y install python
RUN DEBIAN_FRONTEND=noninteractive apt-get update && apt-get install -y python2.7 wget gcc gcc-multilib
RUN wget https://bootstrap.pypa.io/pip/2.7/get-pip.py
RUN python2.7 get-pip.py
RUN python2.7 -m pip install flask==1.0.0
COPY ./flag /flag
RUN chmod 400 /flag

RUN chmod 1733 /tmp /var/tmp /dev/shm
RUN chmod 1733 /proc
RUN chmod 600 $WDIR
RUN chmod +x $WDIR/start.sh

CMD ["/lazy_judge/start.sh"]

EXPOSE 2222