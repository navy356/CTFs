# Use a suitable base-image.
FROM rustlang/rust:nightly-buster

#RUN rustup toolchain install nightly-2021-05-20
#RUN rustup default nightly-2021-05-20

# Create our mapped data volume endpoint
RUN mkdir /data/
RUN mkdir /static/

# Change the working directory.
WORKDIR /service/

# Expose the service's port
EXPOSE 4242

ENV ROCKET_ADDRESS=0.0.0.0
ENV ROCKET_PORT=4242

RUN apt-get update
RUN apt-get install libpq-dev -y
RUN apt-get install netcat -y
RUN cargo install diesel_cli --no-default-features --features postgres

RUN cargo init
COPY ./src/activitytracker/Cargo.* ./
RUN cargo build --release

# Copy our service
COPY ./src/activitytracker/ /service/

COPY ./src/activitytracker/imgs/background.svg /static/background.svg
COPY ./src/activitytracker/imgs/default.jpg /static/default.jpg
RUN touch /service/src/main.rs


#RUN diesel setup
#RUN diesel migration run

RUN ROCKET_ENV=production cargo build --release

COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

# Run the service
#ENTRYPOINT ["cargo", "run"]
#ENTRYPOINT ["cargo", "run", "--release"]
ENTRYPOINT ["/entrypoint.sh"]