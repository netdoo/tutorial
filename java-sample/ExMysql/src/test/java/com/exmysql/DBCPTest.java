package com.exmysql;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBCPTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    BasicDataSource basicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/testdb?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useAffectedRows=true&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("1111");
        dataSource.setInitialSize(10);      // BasicDataSource 클래스 생성 후 최초로 getConnection() 메서드를 호출할 때 커넥션 풀에 채워 넣을 커넥션 개수
        dataSource.setMaxWait(6000);        // BasicDataSource 클래스의 maxWait 속성은 커넥션 풀 안의 커넥션이 고갈됐을 때 커넥션 반납을 대기하는 시간(밀리초)이며 기본값은 무한정
        dataSource.setMaxActive(1);        // 동시에 사용할 수 있는 최대 커넥션 개수(기본값: 8)
        dataSource.setValidationQuery("select 1");
        /**
         * 검증에 지나치게 자원을 소모하지 않게
         * testOnBorrow 옵션과 testOnReturn 옵션은 false로 설정하고,
         * 오랫동안 대기 상태였던 커넥션이 끊어지는 현상을 막게 testWhileIdle 옵션은 true로 설정하는 것을 추천한다.
         * [출처] http://d2.naver.com/helloworld/5102792
         */
        dataSource.setTestOnBorrow(false);       // 커넥션 풀에서 커넥션을 얻어올 때 테스트 실행(기본값: true)
        dataSource.setTestOnReturn(false);      // 커넥션 풀로 커넥션을 반환할 때 테스트 실행(기본값: false)
        dataSource.setTestWhileIdle(true);      // Evictor 스레드가 실행될 때 (timeBetweenEvictionRunMillis > 0) 커넥션 풀 안에 있는 유휴 상태의 커넥션을 대상으로 테스트 실행(기본값: false)
        dataSource.setMaxIdle(1);              // 커넥션 풀에 반납할 때 최대로 유지될 수 있는 커넥션 개수(기본값: 8)
        dataSource.setMinIdle(1);              // 최소한으로 유지할 커넥션 개수(기본값: 0)
        dataSource.setTimeBetweenEvictionRunsMillis(5000);  // Evictor 스레드가 동작하는 간격. 기본값은 -1이며 Evictor 스레드의 실행이 비활성화돼 있다.
        return dataSource;
    }

    @Test
    public void testApp() throws Exception {
        BasicDataSource dataSource = basicDataSource();

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT id, body FROM memo");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            logger.info("id {}, memo {}", rs.getInt("id"), rs.getString("body"));
        }

        if (stmt != null) {
            stmt.close();
        }

        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void _02_idel_타임아웃인경우_테스트() throws Exception {
        BasicDataSource dataSource = basicDataSource();

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT id, body FROM memo");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            logger.info("id {}, memo {}", rs.getInt("id"), rs.getString("body"));
        }

        if (stmt != null) {
            stmt.close();
        }

        // very long batch job
        for (int i = 0; i < 60; i++) {
            Thread.sleep(1_000);
            logger.info("{}", i);
        }

        if (conn != null) {
            conn.close();
        }
    }
}
