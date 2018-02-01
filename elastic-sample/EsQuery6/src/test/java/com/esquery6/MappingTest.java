package com.esquery6;

import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MappingTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(MappingTest.class);
    String indexName = "sample";
    String typeName = "market";
    static TransportClient esClient;

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        //executed only once, before the first test
        esClient = connect();
        List<DiscoveryNode> nodes = esClient.listedNodes();
        nodes.forEach(node -> {
            logger.info("discover node address {}", node.getAddress());
        });
    }

    @Test
    public void _01_인덱스_생성() throws Exception {
        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(indexName).execute().actionGet();

            if (r.isAcknowledged() == true) {
                logger.info("create index {} ", indexName);
            } else {
                logger.error("fail to create index ");
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.info("already exists index {} ", indexName);
        }
    }

    @Test
    public void _02_인덱스_조회() throws Exception {
        String[] indexList = esClient.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getConcreteAllIndices();
        logger.info("index list => {}", indexList);
    }

    @Test
    public void _03_매핑_생성() throws Exception {

        String mappingJson = getResource("MappingTest.txt");

        PutMappingRequest request = new PutMappingRequest(indexName);
        request.type(typeName);
        request.source(mappingJson, XContentType.JSON);
        request.timeout(TimeValue.timeValueMinutes(2));
        PutMappingResponse putMappingResponse = esClient.admin().indices().putMapping(request).actionGet();

        if (putMappingResponse.isAcknowledged()) {
            logger.info("create mapping");
        } else {
            logger.error("fail to create mapping");
        }
    }

    @Test
    public void _04_매핑_삭제() throws Exception {
        /*
        https://www.elastic.co/guide/en/elasticsearch/reference/6.0/indices-delete-mapping.html

        Delete Mappingedit

        It is no longer possible to delete the mapping for a type. Instead you should delete the index and recreate it with the new mappings.
        */
    }

    @Test
    public void _05_매핑_조회() throws Exception {

        final GetMappingsResponse mappings = esClient.admin().indices().prepareGetMappings(indexName).setTypes(typeName).get();
        String mappingJson = mappings.getMappings().get(indexName).get(typeName).source().toString();

        logger.info("mapping size {}", mappings.getMappings().size());
        logger.info("{}", mappingJson);
    }

    @Test
    public void _06_인덱스_삭제() throws Exception {
        DeleteIndexResponse r = esClient.admin().indices().prepareDelete(indexName).execute().actionGet();

        if (r.isAcknowledged() == true) {
            logger.info("delete index {} ", indexName);
        } else {
            logger.error("fail to delete index ");
        }
    }

    @Test
    public void _07_인덱스_조회() throws Exception {
        String[] indexList = esClient.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getConcreteAllIndices();
        logger.info("index list => {}", indexList);
    }
}
