package com.exelasticbatch.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class EsBatchContext {

    //public static String sampleIndexName = "sample";
    //public static String marketTypeName = "market";
    public static String indexName = "dummy";
    public static String typeName = "alphabet";

    public static final String clusterName = "my-application";
    public static final String host = "localhost";
    public static final int port = 9300;

    public static ObjectMapper objectMapper = new ObjectMapper();
}
