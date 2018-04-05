# Elasticsearch Jest Java API

```concept
SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
searchSourceBuilder.query(QueryBuilders.matchAllQuery()).size(1).sort("updateDate", SortOrder.DESC);
Search query = new Search.Builder(searchSourceBuilder.toString()).addIndex("indexName").addType("typeName").build();

try {
    JestResult result = jestClient.execute(query);
    return result.getSourceAsObject(TopSearchResult.class);
} catch (Exception e) {
    logger.error("조회 실패 : ", e.getMessage());
}
```

