# Stage 1: Build the Angular app
FROM node:19.9.0-alpine as build-stage

WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:1.23.2-alpine
COPY ./nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage /usr/src/app/dist/frontend /usr/share/nginx/html
