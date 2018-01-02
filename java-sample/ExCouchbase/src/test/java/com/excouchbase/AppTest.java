package com.excouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final String nodes[] = {"localhost"};
    final String bucketName = "mybucket";
    final String bucketPassword = "1111";

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);


    @Test
    public void _0_테스트_준비() throws Exception {

    }

    @Test
    public void _1_테스트_CRUD() throws Exception {

        Bucket bucket = CouchbaseUtil.openBucket(CouchbaseUtil.createCluster(this.nodes), bucketName, bucketPassword);

        String id = "1";

        JsonObject content = JsonObject.empty()
                .put("type", "gift")
                .put("color", "red")
                .put("price", 1000);

        // create
        JsonDocument document = JsonDocument.create(id, content);

        if (bucket.exists(id)) {
            // update
            JsonDocument upserted = bucket.upsert(document);
        } else {
            // insert
            JsonDocument inserted = bucket.insert(document);
        }

        // get
        JsonDocument retrieved = bucket.get(id);
        String type = retrieved.content().getString("type");
        String color = retrieved.content().getString("color");
        int price = retrieved.content().getInt("price");

        logger.info("{}", retrieved);

        // remove
        JsonDocument removed = bucket.remove(id);
    }
}
