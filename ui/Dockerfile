FROM node:latest as build-stage

ENV NODE_OPTIONS --max_old_space_size=8192

WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY ./ /app/
COPY ./nginx.conf /nginx.conf
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration
# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM nginx:latest
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
# Copy the default nginx.conf provided by tiangolo/node-frontend
COPY --from=build-stage /nginx.conf /etc/nginx/conf.d/default.conf
