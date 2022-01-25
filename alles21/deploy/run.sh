docker run -it -p 8080:8080 --name=jsp jsp
trap "docker rm mdnotes" EXIT
