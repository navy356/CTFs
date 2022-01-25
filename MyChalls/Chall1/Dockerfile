FROM python:latest
FROM mcr.microsoft.com/playwright:focal

WORKDIR /app

COPY requirements.txt ./
COPY *.py ./
RUN mkdir templates
COPY templates/ ./templates/
RUN mkdir uploads

RUN pip install -r requirements.txt

ENV FLASK_APP='app.py'

EXPOSE 1337