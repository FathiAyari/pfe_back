FROM python:3.8-slim
ENV PYTHONUNBUFFER 1
RUN apt-get update
RUN apt-get upgrade -y
RUN mkdir /app
WORKDIR /app
COPY requirements.txt /app/
RUN pip install -r requirements.txt
RUN python -m spacy download fr_core_news_md
COPY . /app/

COPY entrypoint.sh /
RUN chmod +x  /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]