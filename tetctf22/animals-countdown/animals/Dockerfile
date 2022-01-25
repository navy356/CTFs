FROM node:14

USER node

ENV NPM_CONFIG_PREFIX=/home/node/.npm-global
ENV PATH=$PATH:/home/node/.npm-global/bin
WORKDIR /home/node

RUN mkdir -p /home/node/app

WORKDIR /home/node/app

COPY --chown=node:node ./animals/src/ .

RUN npm install

USER root
RUN echo "TetCTF{nodejs_1s_as0m3_pol}" > /flag
RUN chmod 0400 /flag
COPY --chown=node:node ./animals/readflag.c /readflag.c
RUN gcc /readflag.c -o /readflag
RUN chmod 4755 /readflag

USER node

EXPOSE 3000

CMD [ "node", "index.js" ]