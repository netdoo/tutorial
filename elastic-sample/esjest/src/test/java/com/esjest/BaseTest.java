package com.esjest;

import com.esjest.model.AbstractDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.params.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class BaseTest {

    public static JestClient createJestClient() {
        HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://localhost:9200")
                .multiThreaded(true).build();
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(clientConfig);
        return factory.getObject();
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static String getResource(String name) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(BaseTest.class.getResourceAsStream(name)));) {
            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return "";
        }
    }

    public static void readyForTest(String indexName, String typeName, String mappingResPath) throws Exception {
        readyForTest(indexName, typeName, mappingResPath, new ArrayList<>());
    }

    public static void readyForTest(String indexName, String typeName, String mappingResPath, List<? extends AbstractDocument> documentList) throws Exception {
        JestResult deleteIndexResult = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        assertTrue(deleteIndexResult.getErrorMessage(), deleteIndexResult.isSucceeded());

        JestResult createIndexResult = jestClient.execute(new CreateIndex.Builder(indexName).build());
        assertTrue(createIndexResult.getErrorMessage(), createIndexResult.isSucceeded());

        String mappingJson = getResource(mappingResPath);

        PutMapping putMapping = new PutMapping.Builder(indexName, typeName, mappingJson)
                .build();

        JestResult result = jestClient.execute(putMapping);
        assertTrue(result.getErrorMessage(), result.isSucceeded());

        if (!documentList.isEmpty()) {
            Bulk.Builder bulkBuilder = new Bulk.Builder()
                    .defaultIndex(indexName)
                    .defaultType(typeName)
                    .setParameter(Parameters.REFRESH, true);

            for (AbstractDocument document : documentList) {
                bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(document)).index(indexName).type(typeName).id(document.getDocId()).build());
            }

            Bulk bulk = bulkBuilder.build();
            JestResult bulkResult = jestClient.execute(bulk);
            logger.info("bulkResult response code {}", bulkResult.getResponseCode());
        }
    }

    public static JestClient jestClient = createJestClient();
    public static ObjectMapper objectMapper = createObjectMapper();
    final static Logger logger = LoggerFactory.getLogger(BaseTest.class);
}
