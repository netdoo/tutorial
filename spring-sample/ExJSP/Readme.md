# SonarQube 설치

```
https://www.sonarqube.org/downloads/
다운로드 > 압축해제 > 환경변수
환경변수추가 : C:\SonarQube\sonarqube-6.5\bin\windows-x86-64
```

# SonarQube Scanner 설치

```
https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner
다운로드 > 압축해제 > 환경변수
환경변수추가 : C:\SonarQube\sonar-scanner-3.0.3.778-windows\bin
```

# SonarQube 서버 실행

```
cd C:\SonarQube\sonarqube-6.5\bin\windows-x86-64
StartSonart.bat
```

# MVC 프로젝트에 SonarQube Plugin 설치

```java
  </dependencies>
  <build>
    <finalName>exsimpleweb2</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>9.4.0.RC3</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>sonar-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

# MVC 프로젝트 코드 품질 분석

```
mvn clean install sonar:sonar
```

# 프로젝트 코드 품질 분석 결과 확인

```
대쉬보드 접속 :  http://localhost:9000/dashboard/index/com.exsimpleweb2:exsimpleweb2
```

# Jetty 실행

```
clean install -Dmaven.test.skip=true -Djetty.http.port=8080 jetty:run-war
```
