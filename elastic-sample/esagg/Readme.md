
## aggregation 사용시 주의사항

* keyword 필드에만 적용이 가능.
* text 필드에 적용하려면, fielddata = true 로 적용해야 함.

```js
PUT my_index/_mapping/my_type
{
  "properties": {
    "my_field": { 
      "type":     "text",
      "fielddata": true
    }
  }
}
```


## 샘플 색인 생성

```js
PUT order
{
    "mappings" : {
        "history" : {
            "properties" : {
                "create_date" : {"type" : "date"},
                "guest" : {"type" : "keyword"},
                "grade" : {"type" : "keyword"},
                "payment" : {"type" : "long"},
                "menu" : {
                    "type" : "nested",
                    "properties" : {
                        "label" : {"type" : "keyword"},
                        "price" : {"type" : "long"},
                        "count" : {"type" : "byte"},
                        "comment"  : {
                            "type" : "nested",
                            "properties" : {
                                "taste" : {"type" : "keyword"},
                                "season" : {"type" : "keyword"},
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
    "guest" : "james",
    "grade" : "gold",
    "payment" : 11000,
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
    "guest" : "james",
    "grade" : "gold",
    "payment" : 22000,
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
    "guest" : "namsu",
    "grade" : "family",
    "payment" : 21000,
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


PUT order/history/4
{
    "create_date" : "2015-01-03T12:15:30",
    "guest" : "수지",
    "grade" : "family",
    "payment" : 21000,
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


PUT order/history/5
{
    "create_date" : "2015-01-03T12:17:30",
    "guest" : "1234",
    "grade" : "family",
    "payment" : 21000,
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


## guest별로 주문 카운트 집계하는 검색

```js
GET order/history/_search 
{
   "size": 0,
   "aggs": {
      "order_count": {
         "terms": {
            "field": "guest"
         }
      }
   }
}

{
   "took": 4,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 3,
      "max_score": 0,
      "hits": []
   },
   "aggregations": {
      "order_count": {
         "doc_count_error_upper_bound": 0,
         "sum_other_doc_count": 0,
         "buckets": [
            {
               "key": "james",
               "doc_count": 2
            },
            {
               "key": "namsu",
               "doc_count": 1
            }
         ]
      }
   }
}
```

```java
QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
AggregationBuilder aggregationBuilder = AggregationBuilders.terms("order_count").field("guest");

SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
        .setTypes(TYPE_NAME)
        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        .setQuery(queryBuilder)
        .addAggregation(aggregationBuilder);

logger.info("GET {}/{}/_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
SearchResponse r = builder.get();

for (SearchHit hit : r.getHits()) {
    logger.info("\n\n응답 \n{}", hit.getSourceAsString());
}

Aggregations aggregations = r.getAggregations();
Terms terms = aggregations.get("order_count");
Collection<Terms.Bucket> buckets = terms.getBuckets();
buckets.forEach(bucket -> System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
```

## guest별 주문 집계후, guest별 결제금액을 집계하는 검색 

```js
GET order/history/_search 
{
   "size": 0,
   "aggs": {
      "guest_orders": {
         "terms": {
            "field": "guest"
         },
         "aggs": {
            "sum_of_payment": {
               "sum": {
                  "field": "payment"
               }
            }
         }
      }
   }
}
```

```java
AggregationBuilder aggregationBuilder = AggregationBuilders.terms("guest_orders").field("guest")
        .subAggregation(AggregationBuilders.sum("sum_of_payment").field("payment"));

SearchRequestBuilder builder = client.prepareSearch(INDEX_NAME)
        .setTypes(TYPE_NAME)
        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
        .addAggregation(aggregationBuilder);

logger.info("GET {}/{}_search \n{}", INDEX_NAME, TYPE_NAME, builder.toString());
SearchResponse r = builder.get();

Aggregations aggregations = r.getAggregations();
Terms terms = aggregations.get("guest_orders");
Collection<Terms.Bucket> buckets = terms.getBuckets();
buckets.forEach(bucket -> {
    InternalSum sum = bucket.getAggregations().get("sum_of_payment");
    System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount() + ", sum_of_payment : " + sum.getValue());
});
```



## guest별 주문 집계 > 메뉴 집계 > 메뉴별 금액 집계 하는 검색
```js
GET order/history/_search 
{
   "size": 0,
   "aggs": {
      "guest_orders": {
         "terms": {
            "field": "guest"
         },
         "aggs": {
            "menu_hist": {
               "nested": {
                  "path": "menu"
               },
               "aggs": {
                  "menu2": {
                     "terms": {
                        "field": "menu.label"
                     },
                     "aggs": {
                        "sum_of_price": {
                           "sum": {
                              "field": "menu.price"
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
```




## guest별 주문 집계, 결제금액 집계 > 메뉴 집계 > 메뉴별 금액 집계 하는 검색
```js
GET order/history/_search 
{
   "size": 0,
   "aggs": {
      "guest_orders": {
         "terms": {
            "field": "guest"
         },
         "aggs": {
            "menu_hist": {
               "nested": {
                  "path": "menu"
               },
               "aggs": {
                  "menu_label_hist": {
                     "terms": {
                        "field": "menu.label"
                     },
                     "aggs": {
                        "sum_of_price": {
                           "sum": {
                              "field": "menu.price"
                           }
                        }
                     }
                  }
               }
            }
         }
      },
      "guest_payments": {
         "sum": {
            "field": "payment"
         }
      }
   }
}
```

## filter aggregation

```js
GET order/history/_search
{
    "size": 0, 
    "aggs" : {
        "aggs_menu" : {
            "filter" : {
                "term": {
                   "guest": "james"
                }
            },
            "aggs" : {
                "aggs_james" : {
                    "terms" : {
                        "field" : "guest"
                    }
                }
            }
        }
    }
}
```

## nested filter aggregation

```js
GET order/history/_search 
{
   "size": 0,
   "aggregations": {
      "menu_agg": {
         "nested": {
            "path": "menu"
         },
         "aggregations": {
            "label_agg": {
               "filter": {
                  "term": {
                     "menu.label": {
                        "value": "pizza"
                     }
                  }
               },
               "aggregations": {
                  "names": {
                     "terms": {
                        "field": "menu.label"
                     }
                  }
               }
            }
         }
      }
   }
}
```


