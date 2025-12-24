FROM nginx:stable-alpine

WORKDIR /usr/share/nginx/html
COPY target/dist .

RUN apk update && apk add curl
HEALTHCHECK --interval=5s --timeout=4s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

