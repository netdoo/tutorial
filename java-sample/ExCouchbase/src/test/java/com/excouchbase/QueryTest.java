package com.excouchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryTest {

    final String nodes[] = {"localhost"};
    final String bucketName = "mybucket";
    final String bucketPassword = "1111";

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _0_테스트_준비() throws Exception {
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

        bucket.upsert(JsonDocument.create("2", JsonObject.empty().put("type", "gift").put("color", "green").put("price", 2000)));
        bucket.upsert(JsonDocument.create("3", JsonObject.empty().put("type", "gift").put("color", "blue").put("price", 3000)));
        bucket.upsert(JsonDocument.create("4", JsonObject.empty().put("type", "gift").put("color", "black").put("price", 10_000)));
        bucket.upsert(JsonDocument.create("5", JsonObject.empty().put("type", "gift").put("color", "white").put("price", 20_000)));
    }

    @Test
    public void _1_색인_생성() throws Exception {

        Bucket bucket = CouchbaseUtil.openBucket(CouchbaseUtil.createCluster(this.nodes), bucketName, bucketPassword);

        String statement = "CREATE PRIMARY INDEX ON mybucket USING GSI";
        N1qlQuery query = N1qlQuery.simple(statement);
        N1qlQueryResult result = bucket.query(query);
    }

    @Test
    public void _2_쿼리_테스트() throws Exception {

        Bucket bucket = CouchbaseUtil.openBucket(CouchbaseUtil.createCluster(this.nodes), bucketName, bucketPassword);

        String statement = "SELECT type, color, price FROM `mybucket` WHERE price BETWEEN 1000 AND 10000";
        N1qlQuery query = N1qlQuery.simple(statement);
        N1qlQueryResult result = bucket.query(query);

        for (N1qlQueryRow row : result) {
            String type = row.value().getString("type");
            String color = row.value().getString("color");
            long price = row.value().getLong("price");
            logger.info("{} {} {}", type, color, price);
        }

        /**
        실행결과

        gift red 1000
        gift green 2000
        gift blue 3000
        gift black 10000
        */
    }
}
