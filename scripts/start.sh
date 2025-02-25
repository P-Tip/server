#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/app.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 기존 프로세스 종료 (sudo로 실행)
echo "$TIME_NOW > 기존 Java 프로세스 확인 및 종료" >> $DEPLOY_LOG
sudo pkill -f "java -jar"
sleep 3

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# 권한 확인
echo "$TIME_NOW > 실행 권한 부여" >> $DEPLOY_LOG
chmod +x $JAR_FILE

# sudo로 jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행 (sudo 사용)" >> $DEPLOY_LOG
sudo nohup java -jar $JAR_FILE > $APP_LOG 2>&1 &

sleep 5  # 프로세스가 시작될 시간 부여

CURRENT_PID=$(pgrep -f $JAR_FILE)
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 애플리케이션 시작 실패! 로그를 확인하세요." >> $DEPLOY_LOG
  tail -n 50 $APP_LOG >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
fi