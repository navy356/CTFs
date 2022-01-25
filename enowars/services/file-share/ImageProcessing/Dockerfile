FROM imagick/imagemagick:latest

RUN apt-get update && apt-get install inotify-tools -y && rm -rf /var/lib/apt/lists/*

WORKDIR /
COPY convert.sh ./convert.sh
RUN chmod +x convert.sh
ENTRYPOINT ["/convert.sh"]
