
```
 VM Options
  -XX:+PrintGCTimeStamps (GC 발생 시간 정보를 출력)
  -XX:+PrintGCDetails (GC 수행 상세 정보를 출력)
  -Xloggc:file (GC 로그 파일이름 지정, 이게 없으면 바로 console에 출력됨)

  -Xmx32m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log


  http://gceasy.io/ 에 gc.log를 업로드 한 후 분석함.

http://greatkim91.tistory.com/114?category=70917
```
