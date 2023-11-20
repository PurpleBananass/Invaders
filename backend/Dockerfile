# Node.js 이미지를 기반으로 합니다.
FROM node:18

# 앱 디렉토리 생성
USER root
WORKDIR /app

# 앱 의존성 설치
COPY package*.json ./

RUN npm install

# 앱 소스 추가
COPY . .
RUN ls -al

RUN npm run build
EXPOSE 3200
CMD [ "npm", "start" ]