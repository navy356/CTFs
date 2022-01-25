FROM python:3.8

# ENV
ENV user dreamhack
ENV port 8000

# SET packages
#RUN apt-get update -y
#RUN apt-get install -y python-pip python-dev build-essential musl-dev gcc

# SET challenges
RUN adduser --disabled-password $user
ADD ./deploy /app
WORKDIR /app
RUN pip install -r requirements.txt # or # RUN pip install flask
RUN gcc /app/flag.c -o /flag \
    && chmod 111 /flag && rm /app/flag.c

# RUN
USER $user
EXPOSE $port

ENTRYPOINT ["python"]
CMD ["app.py"]