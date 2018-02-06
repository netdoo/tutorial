package com.esquery6.dsl.document;

import com.esquery6.BaseTest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class _4_AliasTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_4_AliasTest.class);

    static String firstIndexName = "first";
    static String secondIndexName = "second";
    static String testTypeName = "test";

    String sampleAliasName = "my_alias";

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        //initSearchTest(logger);

        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(firstIndexName).execute().actionGet();
        } catch (IndexNotFoundException e) {}

        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(secondIndexName).execute().actionGet();
        } catch (IndexNotFoundException e) {}

        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        bulkRequest.add(esClient.prepareIndex(firstIndexName, testTypeName, "1").setSource("{\"name\" : \"mbc\"}", XContentType.JSON));
        bulkRequest.add(esClient.prepareIndex(firstIndexName, testTypeName, "2").setSource("{\"name\" : \"kbs\"}", XContentType.JSON));
        bulkRequest.add(esClient.prepareIndex(secondIndexName, testTypeName, "1").setSource("{\"name\" : \"ebs\"}", XContentType.JSON));
        bulkRequest.add(esClient.prepareIndex(secondIndexName, testTypeName, "2").setSource("{\"name\" : \"sbs\"}", XContentType.JSON));

        BulkResponse r = bulkRequest.execute().actionGet(5000);

        if (r.hasFailures()) {
            logger.error("fail to bulk insert");
        } else {
            logger.info("bulk insert !!");
        }

        esClient.admin().indices().prepareRefresh(firstIndexName, secondIndexName).get();
    }

    @Test
    public void dummy() throws Exception {

    }

    @Test
    public void _01_AddAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices()
                        .prepareAliases()
                        .addAlias(sampleIndexName, sampleAliasName).execute().actionGet();

        if (response.isAcknowledged()) {
            logger.info("add alias {} => {}", sampleIndexName, sampleAliasName);
        } else {
            logger.error("fail to add alias {} => {}", sampleIndexName, sampleAliasName);
        }
    }

    @Test
    public void _02_GetAliasTest() throws Exception {
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
    public void _03_RemoveAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices()
                                            .prepareAliases()
                                            .removeAlias(sampleIndexName, sampleAliasName).execute().actionGet();
        if (response.isAcknowledged()) {
            logger.info("remove alias {} => {}", sampleIndexName, sampleAliasName);
        } else {
            logger.error("fail to remove alias {} => {}", sampleIndexName, sampleAliasName);
        }
    }

    @Test
    public void _04_SwapAliasTest() throws Exception {
        IndicesAliasesResponse response = esClient.admin().indices().prepareAliases()
                                            .removeAlias("old_index", "my_alias")
                                            .addAlias("new_index", "my_alias")
                                            .execute().actionGet();

        if (response.isAcknowledged()) {
            logger.info("swap alias {} => {}", sampleIndexName, sampleAliasName);
        } else {
            logger.error("fail to swap alias {} => {}", sampleIndexName, sampleAliasName);
        }
    }
}
