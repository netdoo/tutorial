## 샘플 색인 생성

```js
PUT order
{
    "mappings" : {
        "history" : {
            "properties" : {
                "create_date" : {"type" : "date"},
                "menu" : {
                    "type" : "nested",
                    "properties" : {
                        "label" : {"type" : "text"},
                        "price" : {"type" : "long"},
                        "count" : {"type" : "byte"},
                        "comment"  : {
                            "type" : "nested",
                            "properties" : {
                                "taste" : {"type" : "text"},
                                "season" : {"type" : "text"},
                                "takeout" : {"type" : "boolean"}
                            }
                        }
                    }
                }
            }
        }
    }
}
```

## 샘플 데이터 입력

```js
PUT order/history/1
{
    "create_date" : "2015-01-01T12:10:30",
    "menu" : [
        {
            "label" : "pizza",
            "price" : 10000,
            "count" : 1,
            "comment" : {
                "taste" : "sweet",
                "season" : "summer",
                "takeout" : true
            }
        },
        {
            "label" : "coke",
            "price" : 1000,
            "count" : 1
        }
    ]
}

PUT order/history/2
{
    "create_date" : "2015-01-01T12:15:10",
    "menu" : [
        {
            "label" : "pizza",
            "price" : 20000,
            "count" : 1,
            "comment" : {
                "taste" : "good sweet",
                "season" : "summer",
                "takeout" : true
            }
        },
        {
            "label" : "coke",
            "price" : 1000,
            "count" : 2
        }
    ]
}

PUT order/history/3
{
    "create_date" : "2015-01-03T12:10:30",
    "menu" : [
        {
            "label" : "pizza",
            "price" : 20000,
            "count" : 1,
            "comment" : {
                "taste" : "sweet",
                "season" : "spring",
                "takeout" : false
            }
        },
        {
            "label" : "lemonade",
            "price" : 1000,
            "count" : 1
        }
    ]
}

```

## 입력된 데이터 확인

```js
GET order/history/_search 
{
    "query": {
        "match_all": {}
    }
}
```





## 2015년 1월 1일 주문내역을 최근순으로 정렬하여 검색 

```js
GET order/history/_search 
{
    "query": {
        "match": {
           "create_date": "2015-01-01"
        }
    },
    "sort": [
       {
          "create_date": {
             "order": "desc"
          }
       }
    ]
}
```




## menu.label이 coke인 주문을 검색

```js
GET order/history/_search 
{
    "query" : {
        "nested": {
           "path": "menu",
           "query": {
               "match": {
                  "menu.label": "coke"
               }
           }
        }
    },
    "sort": [
       {
          "create_date": {
             "order": "asc"
          }
       }
    ]
}
```
```java
    TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

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
```


## menu.comment.takeout이 true인 주문을 검색 

```js
GET order/history/_search 
{
   "query": {
      "nested": {
         "path": "menu.comment",
         "query": {
            "match": {
               "menu.comment.takeout": true
            }
         }
      }
   }
}
```
```java
    TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

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
```

## menu.comment.takeout이 true인 주문을 검색 

```js
GET order/history/_search 
{
    "query": {
        "nested": {
           "path": "menu",
           "query": {
               "nested": {
                  "path": "menu.comment",
                  "query": {
                      "match": {
                         "menu.comment.takeout": true
                      }
                  }
               }
           }
        }
    }
}
```
```java
    TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
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
```

