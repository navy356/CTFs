FROM python:3.6-alpine

# ENV
ENV user dreamhack
ENV port 3333

# SET packages
RUN apk update

# SET challenges
RUN adduser --disabled-password $user
ADD ./deploy /app
WORKDIR /app
RUN pip install -r requirements.txt && adduser $user
RUN chown $user:$user /app

# RUN
USER $user
EXPOSE $port

ENTRYPOINT ["python"]
CMD ["app.py"]

