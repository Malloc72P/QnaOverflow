#!/bin/bash

mkdir -p /home/ec2-user/workspace
mkdir -p /home/ec2-user/workspace/log
mkdir -p /home/ec2-user/workspace/temp
mkdir -p /home/ec2-user/workspace/zip

fuser -k 8080/tcp

rm -rf /home/ec2-user/workspace/temp/*

logname="$(date +%y-%m-%d-%H-%M).log"

cp /home/ec2-user/workspace/zip/deploy-be.zip /home/ec2-user/workspace/temp

unzip /home/ec2-user/workspace/temp/deploy-be.zip -d /home/ec2-user/workspace/temp

touch /home/ec2-user/workspace/log/$logname

nohup java -jar \
        -Dspring.profiles.active=dev \
        /home/ec2-user/workspace/temp/qnaboard-0.0.1-SNAPSHOT.jar \
        > /home/ec2-user/workspace/log/$logname 2>&1 &
