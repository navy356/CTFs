#!/bin/sh
sleep 20 # wait mysql docker
python manage.py makemigrations && python manage.py migrate
uwsgi --ini /app/uwsgi.ini
sleep 365d