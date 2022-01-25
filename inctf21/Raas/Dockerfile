FROM python:3.6-stretch

# We don't want to run our application as root if it is not strictly necessary, even in a container.
# Create a user and a group called 'app' to run the processes.
# A system user is sufficient and we do not need a home.
ADD *.txt /code/
WORKDIR /code
RUN pip install -r requirements.txt

ADD *.py /code/
RUN mkdir /code/modules
RUN mkdir /code/utils
RUN mkdir /code/templates
ADD modules /code/modules/
ADD utils /code/utils/
ADD templates /code/templates

CMD ["python", "app.py"]