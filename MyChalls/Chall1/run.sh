docker run -it -p 80:1337 --name=chall1 chall1 python app.py --host=0.0.0.0
trap "docker rm chall1" EXIT
