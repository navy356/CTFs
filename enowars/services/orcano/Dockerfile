FROM ghcr.io/enowars/enowars5-service-orcano-dolphin

# Install Python dependencies
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
	python3-pip
COPY requirements.txt ./
RUN pip install -r requirements.txt

# Configure Dolphin
# Careful: ~ doesn't work here!
RUN mkdir /root/.dolphin-emu
COPY Config /root/.dolphin-emu/Config

# Configure service
WORKDIR /service
RUN mkdir data
COPY service.py image.dol ./

# Set unbuffered mode so we get stdout
ENV PYTHONUNBUFFERED=1
CMD [ "python3", "service.py" ]