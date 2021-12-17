# 빌드를 위해 백엔드 디렉토리로 이동
cd /var/jenkins_home/workspace/qnaboard-dev/be/qnaboard

# 백엔드 CodeDeploy 스크립트를 루트로 가져옴
mv appspec.yml ../../appspec.yml

# 배포 완료 후 앱을 실행하는 스크립트 가져옴
mkdir -p ../../scripts
cp ./deploy.sh ../../scripts/deploy.sh

# 실행권한을 위해 chmod 수행
chmod +x gradlew

# git repository에는 없는 설정파일 주입
cp /var/jenkins_home/workspace/ignored-settings/* /var/jenkins_home/workspace/qnaboard-dev/be/qnaboard/src/main/resources/

# 빌드
./gradlew clean build

# 워크스페이스 루트로 이동
cd /var/jenkins_home/workspace/qnaboard-dev
mkdir -p deploy
mv ./be/qnaboard/build/libs/qnaboard-0.0.1-SNAPSHOT.jar ./deploy/qnaboard-0.0.1-SNAPSHOT.jar

# 빌드된 jar를 zip으로 압축
cd deploy
zip -r deploy-be.zip *
