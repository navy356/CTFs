FROM node:alpine AS builder


WORKDIR /app

COPY package.json .
COPY yarn.lock .
RUN yarn install

COPY public/ ./public/
COPY src/ ./src/
COPY nginx/ ./nginx
# COPY ./write_ip_into_config.sh ./
# RUN sh write_ip_into_config.sh
RUN yarn build

FROM nginx:stable-alpine
COPY --from=builder /app/build /usr/share/nginx/html
COPY --from=builder /app/nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
