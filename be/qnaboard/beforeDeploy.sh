#!/bin/bash
#릴리즈 폴더 존재시 제거
if [ -d /home/ec2-user/release ]; then
  rm -rf /home/ec2-user/release
fi

#필요한 폴더 생성
mkdir -p /home/ec2-user/release
mkdir -p /home/ec2-user/log
