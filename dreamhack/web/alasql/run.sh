docker run -it -p 80:1337 --name=alasql alasql --host=0.0.0.0
trap "docker rm alasql" EXIT
