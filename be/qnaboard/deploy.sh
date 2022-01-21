#!/bin/bash
#현재 실행중인 서버 중지
sudo fuser -k 8080/tcp
#로그파일의 이름 생성
logname="$(date +%y-%m-%d-%H-%M).log"
#젠킨스에서 빌드해서 업로드한 파일 압축 해제
sudo unzip /home/ec2-user/release/deploy-be.zip -d /home/ec2-user/release/
#서버 실행
sudo nohup java -jar \
        -Dspring.profiles.active=dev \
        /home/ec2-user/release/qnaboard-0.0.1-SNAPSHOT.jar \
        > /home/ec2-user/log/$logname 2>&1 &
