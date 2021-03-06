package com.excouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConnTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _1_접속테스트() throws Exception {

        String nodes[] = {"localhost"};
        String bucketName = "mybucket";
        String bucketPassword = "1111";

        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(10000)
                .kvTimeout(3000)
                .build();

        Cluster cluster = CouchbaseCluster.create(env, nodes);
        Bucket bucket = cluster.openBucket(bucketName, bucketPassword);
        logger.info("connect success {}", bucket.name());
        JsonDocument document = bucket.get("1234");
        logger.info("{}", document);
    }
}
