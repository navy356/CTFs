docker run -it -p 80:80 --name=dist2 dist2
trap "docker rm dist2" EXIT
