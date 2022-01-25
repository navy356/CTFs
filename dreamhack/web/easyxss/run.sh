docker run -it -p 80:80 --name=easyxss easyxss
trap "docker rm easyxss" EXIT
