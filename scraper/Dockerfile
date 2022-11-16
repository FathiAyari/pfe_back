FROM node:current-alpine as noderunner
WORKDIR /app
COPY scripts/*.json ./
RUN npm install

FROM noderunner
WORKDIR /app
COPY scripts/*.js ./
COPY config/cronjobs /etc/crontabs/root
RUN mkdir /var/output
VOLUME /var/output

LABEL app=cron-scripts

# start crond with log level 8 in foreground, output to stderr
CMD ["crond", "-f", "-d", "8"]