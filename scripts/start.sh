#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
BUILD_DIR="$PROJECT_ROOT/build/libs"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date '+%Y-%m-%d %H:%M:%S')
JAR_NAME=$(ls $BUILD_DIR/*.jar | tail -n 1)
DEPLOY_JAR="$PROJECT_ROOT/spring-webapp.jar"

echo "$TIME_NOW > 배포 시작" >> $DEPLOY_LOG

# 1. 기존 파일 제거 (선택)
rm -f $DEPLOY_JAR

# 2. build/libs 에서 JAR 복사
echo "$TIME_NOW > $JAR_NAME → $DEPLOY_JAR 복사" >> $DEPLOY_LOG
cp $JAR_NAME $DEPLOY_JAR

# 3. 권한 설정
chmod +x $DEPLOY_JAR

# 4. systemd 재시작
echo "$TIME_NOW > Spring Boot 앱 재시작" >> $DEPLOY_LOG
sudo systemctl restart springboot-app

echo "$TIME_NOW > 배포 완료" >> $DEPLOY_LOG
