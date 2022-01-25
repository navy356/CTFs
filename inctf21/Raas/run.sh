docker run -it -p 5000:5000 --name=raas raas
trap "docker rm raas" EXIT
