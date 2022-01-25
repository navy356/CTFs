FROM python:3.8.1
LABEL version="0.0.1"

WORKDIR /service

COPY logger/requirements.txt /service/requirements.txt
RUN pip3 install -r /service/requirements.txt

COPY logger /service

EXPOSE 80

RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "gunicorn", "-c", "gunicorn.conf.py", "main:app"]
