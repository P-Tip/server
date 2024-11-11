# 빌드 스테이지
FROM openjdk:17-slim AS builder
WORKDIR /app

# 소스 코드 및 Gradle 래퍼 파일 복사
COPY . .

RUN chmod +x ./gradlew

# Gradle을 사용하여 애플리케이션 빌드
RUN ./gradlew build

# 실행 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/ptip-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

