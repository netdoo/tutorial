package com.exmemcached;

import com.google.code.ssm.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class App {
    final String MEMCACHE_NAMESPACE = "MyCacheSpace";
    public static final Logger logger = Logger.getLogger(App.class);

    @Autowired
    SomeDB someDB;

    @ReadThroughSingleCache(namespace = MEMCACHE_NAMESPACE, expiration = 1000)
    public SomeData getSomeData(@ParameterValueKeyProvider long id) throws Exception {
        SomeData result = this.someDB.find(id);
        logger.info("캐쉬에 추가됨 ID : " + id);
        return result;
    }

    @ReadThroughMultiCache(namespace = MEMCACHE_NAMESPACE, expiration = 1000)
    public List<SomeData> getSomeDataList(@ParameterValueKeyProvider List<Long> ids) throws Exception {
        List<SomeData> resultList = this.someDB.find(ids);
        logger.info("캐쉬에 여러개가 추가됨 ID : " + ids);
        return resultList;
    }

    @InvalidateSingleCache(namespace = MEMCACHE_NAMESPACE)
    public void removeCache(@ParameterValueKeyProvider long id) {
        logger.info("캐쉬가 제거됨 ID : " + id);
    }

    @UpdateSingleCache(namespace = MEMCACHE_NAMESPACE, expiration = 10)
    public void updateCache(@ParameterValueKeyProvider long id,
                                   @ParameterDataUpdateContent final SomeData overrideData) {
        logger.info("캐쉬가 업데이트 됨 ID : " + id);
    }

    public static void main( String[] args ) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        App app = context.getBean(App.class);

        /// #1. 캐쉬 읽기
        for (int i = 0; i < 4; i++) {
            SomeData data1 = app.getSomeData(i);
        }

        SomeData someData = app.getSomeData(1);
        List<SomeData> someDataList1 = app.getSomeDataList(new ArrayList<Long>(Arrays.asList(4L,5L,6L)));
        List<SomeData> someDataList2 = app.getSomeDataList(new ArrayList<Long>(Arrays.asList(4L,5L,6L)));

        try {
            SomeData noSuchData = app.getSomeData(123);
        } catch(Exception e) {
            logger.error("no such cache ", e);
        }

        /// #2. 캐쉬 제거
        app.removeCache(1);

        /// #3. 캐쉬 추가
        SomeData data3 = app.getSomeData(1);

        /// #4. 캐쉬 갱신
        app.updateCache(1, new SomeData(1, "james dean"));

        /// #5 캐쉬 갱신 확인
        SomeData data4 = app.getSomeData(1);
        logger.info(data4.getName());

        /// #6 캐쉬 expire
        Thread.sleep(10000);
        SomeData data5 = app.getSomeData(1);
        logger.info(data5.getName());
    }
}
