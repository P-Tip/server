#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/server"
JAR_FILE_1="$PROJECT_ROOT/spring-webapp.jar"
JAR_FILE_2="ptip-0.0.1-SNAPSHOT.jar"  # 절대 경로 없이 파일명만으로도 확인

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 pid 확인 (두 가지 패턴 모두 확인)
CURRENT_PID_1=$(pgrep -f $JAR_FILE_1)
CURRENT_PID_2=$(pgrep -f $JAR_FILE_2)

# 프로세스가 켜져 있으면 종료
if [ -z "$CURRENT_PID_1" ] && [ -z "$CURRENT_PID_2" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다" >> $DEPLOY_LOG
else
  if [ ! -z "$CURRENT_PID_1" ]; then
    echo "$TIME_NOW > 실행중인 $CURRENT_PID_1 애플리케이션 종료 " >> $DEPLOY_LOG
    kill -15 $CURRENT_PID_1
  fi
  if [ ! -z "$CURRENT_PID_2" ]; then
    echo "$TIME_NOW > 실행중인 $CURRENT_PID_2 애플리케이션 종료 " >> $DEPLOY_LOG
    kill -15 $CURRENT_PID_2
    # root로 실행된 프로세스를 위해 sudo 사용
    sudo pkill -f $JAR_FILE_2
  fi
fi