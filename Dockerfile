FROM  openjdk:21

RUN mkdir -p /usr/src/xbrain-app

COPY desafio-venda-xbrain-0.0.1-SNAPSHOT.jar /usr/src/xbrain-app

WORKDIR /usr/src/xbrain-app

EXPOSE 8080

RUN useradd -ms /bin/bash xbrain-challenge

CMD ["java", "-jar", "desafio-venda-xbrain-0.0.1-SNAPSHOT.jar"]