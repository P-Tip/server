#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date '+%Y-%m-%d %H:%M:%S')

echo "$TIME_NOW > Spring Boot 앱 systemd 중지" >> $DEPLOY_LOG
sudo systemctl stop springboot-app
