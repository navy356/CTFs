FROM python:3.8.1
LABEL version="0.0.1"

WORKDIR /service

COPY backend/requirements.txt /service/requirements.txt
RUN pip3 install -r /service/requirements.txt

COPY backend /service

EXPOSE 5000

ENTRYPOINT ["gunicorn", "-c", "gunicorn.conf.py", "main:app"]
