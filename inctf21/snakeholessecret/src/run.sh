docker run -it -p 3000:3000 --name=notepad1 notepad1
trap "docker rm notepad1" EXIT
