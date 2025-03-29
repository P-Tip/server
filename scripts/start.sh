#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date '+%Y-%m-%d %H:%M:%S')

echo "$TIME_NOW > Spring Boot 앱 systemd 재시작" >> $DEPLOY_LOG
sudo systemctl restart springboot-app
