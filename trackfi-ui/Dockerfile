FROM node:23-alpine AS build

WORKDIR /app

COPY . .

RUN npm install && npm run build

FROM nginx:stable-alpine

COPY --from=build /app/target/dist /usr/share/nginx/html

RUN apk update && apk add curl
HEALTHCHECK --interval=5s --timeout=4s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

