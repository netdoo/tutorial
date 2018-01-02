package com.excouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BucketTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _1_버킷_CRUD_테스트() throws Exception {

        String nodes[] = {"localhost"};
        String bucketName = "my_service_bucket";
        String bucketPassword = "my_service_bucket_password";

        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(10000)
                .kvTimeout(3000)
                .build();

        Cluster cluster = CouchbaseCluster.create(env, nodes);
        ClusterManager clusterManager = cluster.clusterManager("Administrator", "123456");
        BucketSettings bucketSettings = new DefaultBucketSettings.Builder()
                .type(BucketType.COUCHBASE)
                .name(bucketName)
                .quota(110)
                .build();

        // create
        clusterManager.insertBucket(bucketSettings);

        // remove
        clusterManager.removeBucket(bucketName);
    }
}
