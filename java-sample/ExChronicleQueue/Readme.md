# Chronicle Queue 특징

* 초당 3만건 벌크로 큐잉이 가능함.
* 일(Day)/시간(Hour)/분(Minute) 단위 큐 파일 롤링이 가능함.

# Chronicle Queue Rolling 예제

```java
try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path)
    .rollCycle(RollCycles.MINUTELY)     // 큐 파일 롤링 주기를 설정하면
    .storeFileListener(new StoreFileListener() {
    @Override
    public void onReleased(int i, File file) {
        if (file != null) {
            // 큐 파일이 롤링 될 때 onReleased 콜백함수를 통해서, 
            // 현재 롤링되는 큐파일의 정보가 전달됨.
            oldFiles.put(i, file);
            logger.info("release file {} cycle {}", file.getName(), i);
        }
    }
}).build()) {

    ExcerptAppender appender = queue.acquireAppender();

    for (int i = 0; i < 15; i++) {
        appender.writeText(String.valueOf(i));
        logger.info("WRITER {} lastCycle {}", i, queue.lastCycle());
        lastCycle = queue.lastCycle();

        if (i == 0) {
            reader.start();
        }
        Thread.sleep(5_000);
    }
}

// 가장 최신 큐 파일(lastCycle)을 제외하고 삭제하기 위해서는 다음과 같이 함. 
// 이 때, 롤링된 큐 파일을 접근하는 READER, WRITER가 있는 경우, 파일 삭제가 실패함.
for (Map.Entry<Integer, File> entry : oldFiles.entrySet()) {
    if (entry.getKey() != lastCycle) {
        File qFile = entry.getValue();
        logger.info("delete file {} result {}", qFile.getName(), qFile.delete());
    }
}
```

# Chronicle Queue에서 이어서 읽기

* 현재 위치는 index() 함수를 통해서 확인할 수 있음.
* 읽기 위치는 moveToIndex() 함수를 통해서 이동할 수 있음.

```java
long lastIndex = 0;

try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
    ExcerptTailer tailer = queue.createTailer();
    logger.info("{}", tailer.readText());
    logger.info("{}", tailer.readText());
    
    // 마지막 읽어들인 위치를 기록함.
    lastIndex = tailer.index();
}

try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
    ExcerptTailer tailer = queue.createTailer();
    
    // 앞에서 읽어들인 위치로 Index를 이동함.
    tailer.moveToIndex(lastIndex);
    
    // 그리고, 그 다음 위치부터 읽어들임.
    logger.info("{}", tailer.readText());
    logger.info("{}", tailer.readText());
    lastIndex = tailer.index();
}
    
```
