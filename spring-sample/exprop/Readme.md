
# Maven Profile 설정

maven profile 을 이용하여 빌드 시, 외부에서 넣어주는 파라미터를 기준으로 
서로 다른 설정파일을 참조하게 하여 환경 별로 다른 설정파일을 사용할 수 있게끔 만드는 기능.



# pom.xml

```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <env>dev</env>
        </properties>
    </profile>
    <profile>
        <id>local</id>
        <properties>
            <env>local</env>
        </properties>
    </profile>
    <profile>
        <id>real</id>
        <properties>
            <env>real</env>
        </properties>
    </profile>
</profiles>

<build> 
    <resources>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
        <resource>
            <directory>src/main/resources-${env}</directory>
        </resource>
    </resources>
</build>
```


# Profile에 맞는 리소스를 classpath에 포함

mvn 명령어의 -P 인자를 활용함.


```sh
$ mvn clean package -P dev
```



# Java로 Profile 조회



```java
Properties prop = new Properties();
prop.load(Some.class.getClassLoader().getResourceAsStream("config.properties"));
String host = prop.getProperty("host");
String port = prop.getProperty("port");
```



