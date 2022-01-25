docker run -it -p 8080:8080 --name=mdnotes mdnotes
trap "docker rm mdnotes" EXIT
