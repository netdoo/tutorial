package com.esquery6.dsl.document;

import com.esquery6.BaseTest;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.action.admin.indices.AliasesNotFoundException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _4_AliasTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_4_AliasTest.class);

    static String firstIndexName = "first";
    static String secondIndexName = "second";
    static String testTypeName = "test";

    static String sampleAliasName = "my_alias";

    static String makeJson(String key, String value) throws Exception {
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field(key, value)
                .endObject();

        return builder.string();
    }

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);

        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(firstIndexName).execute().actionGet();
        } catch (ResourceAlreadyExistsException e) {}

        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(secondIndexName).execute().actionGet();
        } catch (ResourceAlreadyExistsException e) {}

        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        bulkRequest.add(esClient.prepareIndex(firstIndexName, testTypeName, "1").setSource(makeJson("name", "mbc"), XContentType.JSON));
        bulkRequest.add(esClient.prepareIndex(firstIndexName, testTypeName, "2").setSource(makeJson("name", "kbs"), XContentType.JSON));
        bulkRequest.add(esClient.prepareIndex(secondIndexName, testTypeName, "1").setSource(makeJson("name", "ebs"), XContentType.JSON));
        bulkRequest.add(esClient.prepareIndex(secondIndexName, testTypeName, "2").setSource(makeJson("name", "sbs"), XContentType.JSON));

        BulkResponse r = bulkRequest.execute().actionGet(5000);

        if (r.hasFailures()) {
            logger.error("fail to bulk insert");
        } else {
            logger.info("bulk insert !!");
        }

        try {
            IndicesAliasesResponse response = esClient.admin().indices()
                    .prepareAliases()
                    .removeAlias(firstIndexName, sampleAliasName)
                    .removeAlias(secondIndexName, sampleAliasName)
                    .execute().actionGet();
        } catch (AliasesNotFoundException e) {}

        esClient.admin().indices().prepareRefresh(firstIndexName, secondIndexName).get();
    }

    void printAliasDocument() {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleAliasName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchAllQuery());

        debugReqRes(builder, logger);
    }

    @Test
    public void _01_AddAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices()
                        .prepareAliases()
                        .addAlias(firstIndexName, sampleAliasName).execute().actionGet();

        if (response.isAcknowledged()) {
            logger.info("add alias {} => {}", firstIndexName, sampleAliasName);
        } else {
            logger.error("fail to add alias {} => {}", firstIndexName, sampleAliasName);
        }

        // kbs, mbc 만 출력됨.
        printAliasDocument();
    }

    @Test
    public void _02_SwapAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices().prepareAliases()
                .removeAlias(firstIndexName, sampleAliasName)
                .addAlias(secondIndexName, sampleAliasName)
                .execute().actionGet();

        if (response.isAcknowledged()) {
            logger.info("swap alias {} => {}", firstIndexName, secondIndexName);
        } else {
            logger.error("fail to swap alias {} => {}", firstIndexName, secondIndexName);
        }

        // ebs, sbs 만 출력됨.
        printAliasDocument();
    }

    @Test
    public void _03_AddDupAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices()
                .prepareAliases()
                .addAlias(firstIndexName, sampleAliasName)
                .addAlias(secondIndexName, sampleAliasName).execute().actionGet();

        if (response.isAcknowledged()) {
            logger.info("add alias {} => {}", secondIndexName, sampleAliasName);
        } else {
            logger.error("fail to add alias {} => {}", secondIndexName, sampleAliasName);
        }

        // mbc, kbs, sbs, ebs 모두 출력됨.
        printAliasDocument();
    }

    @Test
    public void _04_GetAliasTest() throws Exception {
        // get name of the current index where the alias is active
        GetAliasesResponse r = esClient.admin().indices().getAliases(new GetAliasesRequest()).get();
        ImmutableOpenMap<String, List<AliasMetaData>> aliases = r.getAliases();

        for(Iterator<String> it = aliases.keysIt(); it.hasNext();) {
            String indexName = it.next();
            List<AliasMetaData> metaDatas = aliases.get(indexName);
            List<String> aliasList = metaDatas.stream().map(AliasMetaData::alias).collect(Collectors.toList());

            logger.info("{} => {}", indexName, aliasList);

            metaDatas.forEach(aliasMetaData -> {
                // iterate alias metadata in here !!
            });
        }
    }


    @Test
    public void _09_RemoveAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices()
                                            .prepareAliases()
                                            .removeAlias(firstIndexName, sampleAliasName)
                                            .removeAlias(secondIndexName, sampleAliasName)
                                            .execute().actionGet();
        if (response.isAcknowledged()) {
            logger.info("remove alias {} => {}", firstIndexName, sampleAliasName);
            logger.info("remove alias {} => {}", secondIndexName, sampleAliasName);
        } else {
            logger.error("fail to remove alias {} => {}", firstIndexName, sampleAliasName);
            logger.error("fail to remove alias {} => {}", secondIndexName, sampleAliasName);
        }
    }



}
