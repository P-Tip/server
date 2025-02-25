#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/app.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 실행 중인 이전 프로세스 확인 및 종료
OLD_PID=$(pgrep -f "ptip-0.0.1-SNAPSHOT.jar")
if [ ! -z "$OLD_PID" ]; then
  echo "$TIME_NOW > 이전 버전의 애플리케이션($OLD_PID)이 여전히 실행 중입니다. 종료합니다." >> $DEPLOY_LOG
  kill -15 $OLD_PID
  # root로 실행된 경우를 위해
  sudo pkill -f "ptip-0.0.1-SNAPSHOT.jar"
  sleep 5
fi

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# 권한 확인
echo "$TIME_NOW > 실행 권한 부여" >> $DEPLOY_LOG
chmod +x $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

sleep 5  # 프로세스가 시작될 시간 부여

CURRENT_PID=$(pgrep -f $JAR_FILE)
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 애플리케이션 시작 실패! 로그를 확인하세요." >> $DEPLOY_LOG
  tail -n 50 $ERROR_LOG >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
fi