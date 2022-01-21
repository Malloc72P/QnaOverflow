#!/bin/bash
#릴리즈 폴더 존재시 제거
if [ -d /home/ec2-user/release ]; then
  sudo rm -rf /home/ec2-user/release
fi

#필요한 폴더 생성
sudo mkdir -p /home/ec2-user/release
sudo mkdir -p /home/ec2-user/log
