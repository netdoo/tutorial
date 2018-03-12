
# local 환경 패키지 

```
mvn clean package -P local
mvn exec:java -Dexec.mainClass="com.sample.App" -Dexec.args="arg0 arg1 arg2"
```

# dev 환경 패키지

```
mvn clean package -P dev 
mvn exec:java -Dexec.mainClass="com.sample.App" -Dexec.args="arg0 arg1 arg2"  
```


# real 환경 패키지

```
mvn clean package -P real
mvn exec:java -Dexec.mainClass="com.sample.App" -Dexec.args="arg0 arg1 arg2"
```

# reference

```
https://printf.kr/archives/373
```
