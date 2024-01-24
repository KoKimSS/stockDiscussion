# stockDiscussion
It's stockDisucussion made with SpringBoot, MySql

Docker Compose 명령어

# 표준 출력(stdout), 표준에러(stderr) 출력
docker logs node-test 
…
 

# 로그를 계속 출력
docker logs –f node-test
…
…

# 출력된 로그는 파일로 관리되기 때문에 HostOS 의 disk 를 사용
docker info | grep -i log

# 컨테이너 명령 테스트
cd ~
mkdir nodejsapp
cd nodejsapp
vi app.js # 테스트용 nodejs 앱
vi Dockerfile # 새로운 도커 이미지를 위한 Dockerfile
docker buildx build -t node-test:1.0 . # 1.0 태그를 추가하여 node-test라는 이미지를 빌드 
docker images | grep node-test  # 빌드 완료한 이미지 보기
docker image history node-test:1.0 # 1.0으로 태그 추가한 이미지의 Dockerfile history
docker run -itd -p 6060:6060 --name=node-test -h node-test node-test:1.0
docker ps | grep node-test
curl http://localhost:6060
