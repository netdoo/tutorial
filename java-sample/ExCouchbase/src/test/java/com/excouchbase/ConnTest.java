package com.excouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _1_접속테스트() throws Exception {

        String nodes[] = {"devmem1.tmonc.net"};
        String bucketName = "search_service_bucket";
        String bucketPassword = "search_service_bucket!@#$";

        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(10000)
                .kvTimeout(3000)
                .build();

        Cluster cluster = CouchbaseCluster.create(env, nodes);
        Bucket bucket = cluster.openBucket(bucketName, bucketPassword);
        JsonDocument document = bucket.get("1100181964894961360");
        logger.info("{}", document);
    }
}
