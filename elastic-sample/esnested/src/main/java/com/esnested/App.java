package com.esnested;


import javafx.scene.control.TreeSortMode;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class App {

    final String INDEX_NAME = "order";
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

    public void existIndex() {
        IndicesExistsResponse r = client.admin().indices().prepareExists(INDEX_NAME).execute().actionGet();

        if (r.isExists() == true) {
            logger.info("Exist Index {} ", INDEX_NAME);
        }
    }

    public void doQuery(SearchRequestBuilder builder) {

        logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    public void nestedQuery1() {

        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
            "menu", matchQuery("menu.label", "coke"),
            ScoreMode.None
        );

        SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addSort("create_date", SortOrder.ASC);

        logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    public void nestedQuery2() {
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
            "menu.comment", matchQuery("menu.comment.takeout", true),
            ScoreMode.None
        );

        SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        System.out.println("================================");
    }

    public void nestedQuery3() {
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
            "menu", QueryBuilders.nestedQuery(
                "menu.comment", matchQuery("menu.comment.takeout", true),
                ScoreMode.None
            ),
            ScoreMode.None
        );

        SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    public void nestedQuery4() {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(termQuery("guest", "james"))
                .must(nestedQuery(
                        "menu",
                         nestedQuery(
                             "menu.comment",
                              termQuery("menu.comment.taste", "sweet"),
                              ScoreMode.None
                         ),
                         ScoreMode.None
                    )
                );

        SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 id : {} \n{}", hit.getId(), hit.getSourceAsString());
        }
    }

    public void nestedQuery5() {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(termQuery("guest", "james"))
                .must(nestedQuery(
                        "menu.comment",
                        termQuery("menu.comment.taste", "sweet"),
                        ScoreMode.None
                        )
                );

        SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 id : {} \n{}", hit.getId(), hit.getSourceAsString());
        }
    }


    public static void main( String[] args ) throws Exception {
        App app = new App();
        app.existIndex();
        app.nestedQuery1();
        app.nestedQuery2();
        app.nestedQuery3();
        app.nestedQuery4();
        app.nestedQuery5();
        app.close();
    }
}
