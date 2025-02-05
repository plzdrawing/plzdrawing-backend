#!/bin/bash

# JAR 파일명을 deploy.log에 기록.
JAR_DIR_PATH=/home/ec2-user/plz/build/libs/
BUILD_JAR=$(ls $JAR_DIR_PATH*.jar | grep -nv 'plain')
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 경로: $BUILD_JAR" >> /home/ec2-user/plz/deploy.log
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/plz/deploy.log

# 빌드된 JAR 파일을 /home/ec2-user/ 디렉토리로 복사
echo "> build 파일 복사" >> /home/ec2-user/plz/deploy.log
DEPLOY_PATH=/home/ec2-user/plz/
#cp $BUILD_JAR $DEPLOY_PATH
cp $JAR_DIR_PATH$JAR_NAME $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/plz/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 현재 실행 중인 애플리케이션 종료
if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/plz/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 새로운 JAR 파일 배포 및 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/plz/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/plz/deploy.log 2>/home/ec2-user/plz/deploy_err.log &
