package com.excouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class CouchbaseUtil {
    public static Cluster createCluster(String[] nodes) {
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(10000)
                .kvTimeout(3000)
                .build();

        return CouchbaseCluster.create(env, nodes);
    }

    public static Bucket openBucket(Cluster cluster, String bucketName, String bucketPassword) {
        return cluster.openBucket(bucketName, bucketPassword);
    }
}
