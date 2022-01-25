FROM tiangolo/uwsgi-nginx-flask:python3.8

# ENV
ENV port 80

# SET packages
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub --no-check-certificate | apt-key add - && echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | tee /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update -y
RUN apt-get install -y python-pip python-dev build-essential google-chrome-stable libnss3
RUN wget https://chromedriver.storage.googleapis.com/`curl -sS https://chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip && unzip chromedriver_linux64.zip && rm chromedriver_linux64.zip

# SET challenges
ADD ./ /app
WORKDIR /app
RUN pip install -r requirements.txt
RUN ls -al
EXPOSE $port