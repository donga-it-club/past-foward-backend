#!/bin/bash
export SPRING_PROFILES_ACTIVE=prod

BUILD_JAR=$(ls /home/ec2-user/action/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/action/deploy.log

echo "> build 파일 복사" >> /home/ec2-user/action/deploy.log
DEPLOY_PATH=/home/ec2-user/action/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/action/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/action/deploy.log
else
  echo "> sudo kill -15 $CURRENT_PID" >> /home/ec2-user/action/deploy.log
  kill -15 $CURRENT_PID
  sleep 10

  while ps -p $CURRENT_PID > /dev/null; do
    echo "> 애플리케이션 종료 대기 중..." >> /home/ec2-user/action/deploy.log
    sudo kill -15 $CURRENT_PID
    sleep 5
  done

  echo "> 애플리케이션이 성공적으로 종료되었습니다." >> /home/ec2-user/action/deploy.log
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/action/deploy.log
nohup java -jar $DEPLOY_JAR --spring.profiles.active=prod >> /home/ec2-user/deploy.log 2>/home/ec2-user/action/deploy_err.log &

NEW_PID=$!
echo "$(date) > 새 애플리케이션 PID: $NEW_PID" >> /home/ec2-user/action/deploy.log

echo "$(date) > 애플리케이션 시작 확인" >> /home/ec2-user/action/deploy.log
sleep 10
CHECK_PID=$(pgrep -f $JAR_NAME)
if [ -z "$CHECK_PID" ]
then
  echo "$(date) > 애플리케이션 시작 실패" >> /home/ec2-user/action/deploy.log
else
  echo "$(date) > 애플리케이션 시작 성공, PID: $CHECK_PID" >> /home/ec2-user/action/deploy.log
fi