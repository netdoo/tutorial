package com.exmemcache;

import com.exmemcache.config.MemcachedConfig;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MemcachedConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClusterTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void onBeforeClass() throws Exception {

    }

    @Test
    public void _0_테스트_준비() throws Exception {

    }

    @Test
    public void _01_ClusterTest() throws Exception {
        InetSocketAddress firstServerAddr = new InetSocketAddress("localhost", 11222);
        InetSocketAddress secondServerAddr = new InetSocketAddress("localhost", 11223);

        List<InetSocketAddress> addressList = new ArrayList<>();
        addressList.add(firstServerAddr);
        addressList.add(secondServerAddr);

        ConnectionFactory connectionFactory = new ConnectionFactoryBuilder()
                .setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT).build();

        MemcachedClient clusterClient = new MemcachedClient(connectionFactory, addressList);

        MemcachedClient firstClient = new MemcachedClient(firstServerAddr);
        MemcachedClient secondClient = new MemcachedClient(secondServerAddr);

        int expireSecs = 5000;
        String key = String.valueOf(System.currentTimeMillis());
        String value = "THIS IS TEST VALUE";

        clusterClient.set(key, expireSecs, value);
        logger.info("clusterClient set {}/{}", key, value);

        value = (String)clusterClient.get(key);
        logger.info("clusterClient get {}/{}", key, value);

        /// firstClient 또는 secondClient 둘 중 하나만 데이터 조회가 됨.
        value = (String)firstClient.get(key);
        logger.info("firstClient get {}/{}", key, value);

        value = (String)secondClient.get(key);
        logger.info("secondClient get {}/{}", key, value);
    }

    @Test
    public void _02_ReplicationTest() throws Exception {
        InetSocketAddress firstServerAddr = new InetSocketAddress("localhost", 11222);
        InetSocketAddress secondServerAddr = new InetSocketAddress("localhost", 11223);

        List<InetSocketAddress> addressList = new ArrayList<>();
        addressList.add(firstServerAddr);
        addressList.add(secondServerAddr);

        ConnectionFactory connectionFactory = new ConnectionFactoryBuilder()
                .setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT).build();

        MemcachedClient clusterClient = new MemcachedClient(connectionFactory, addressList);

        MemcachedClient firstClient = new MemcachedClient(firstServerAddr);
        MemcachedClient secondClient = new MemcachedClient(secondServerAddr);

        int expireSecs = 5000;
        String key = String.valueOf(System.currentTimeMillis());
        String value = "THIS IS TEST VALUE";

        firstClient.set(key, expireSecs, value);
        logger.info("firstClient set {}/{}", key, value);

        secondClient.set(key, expireSecs, value);
        logger.info("secondClient set {}/{}", key, value);

        // 각각 개별적으로 값을 입력한 경우, 클러스터에서는 값을 조회할 수 없음.
        value = (String)clusterClient.get(key);
        logger.info("clusterClient get {}/{}", key, value);

        /// 반면에, firstClient 또는 secondClient 둘 다 데이터 조회가 됨.
        value = (String)firstClient.get(key);
        logger.info("firstClient get {}/{}", key, value);

        value = (String)secondClient.get(key);
        logger.info("secondClient get {}/{}", key, value);
    }
}
