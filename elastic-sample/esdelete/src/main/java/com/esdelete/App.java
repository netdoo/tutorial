package com.esdelete;

import java.net.InetAddress;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class App {
    final String INDEX_NAME = "some";
    final String TYPE_NAME = "history";
    final Logger logger = LoggerFactory.getLogger(App.class);

    TransportClient client;

    public App() throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    public void close() {
        client.close();
        logger.info("close client");
    }

    public void insertDocuments(Collection<String> uids) throws Exception {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        for (String uid : uids) {
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(INDEX_NAME, TYPE_NAME, uid);
            indexRequestBuilder.setSource("{}");
            bulkRequestBuilder.add(indexRequestBuilder);
        }

        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            logger.error("fail to insert uids {}", uids);
        } else {
            logger.info("insert uids {}", uids);
        }
    }


    public void deleteDocuments(Collection<String> uids) throws Exception {

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        for (String uid : uids) {
            DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(INDEX_NAME, TYPE_NAME, uid);
            bulkRequestBuilder.add(deleteRequestBuilder);
        }

        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            logger.error("fail to delete uids {}", uids);
        } else {
            logger.info("delete uids {}", uids);
        }
    }

    public void printDocuments() throws Exception {
        SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchAllQuery());

        SearchResponse r = builder.get();
        ArrayList<String> uids = new ArrayList<>();

        r.getHits().forEach(hit -> {
            uids.add(hit.getId());
        });

        logger.info("remain uids : {} ", uids);
    }

    public static void main( String[] args ) throws Exception {
        App app = new App();
        ArrayList<String> insertUids = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        ArrayList<String> deleteUids = new ArrayList<>(Arrays.asList("1", "2", "3"));

        app.insertDocuments(insertUids);
        app.deleteDocuments(deleteUids);
        app.printDocuments();
    }
}
