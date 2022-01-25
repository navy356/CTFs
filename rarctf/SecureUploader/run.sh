docker run -it -p 1337:1337 --name=fileu fileu /app/start.sh --host=0.0.0.0
trap "docker rm fileu" EXIT
