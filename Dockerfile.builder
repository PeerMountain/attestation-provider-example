FROM openjdk:11

RUN apt-get update
RUN apt-get install -y libsodium-dev
RUN curl -L get.web3j.io | sh && source ~/.web3j/source.sh
RUN web3j -v
