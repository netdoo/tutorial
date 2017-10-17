# Elasticsearch 검색 결과를 CSV 로 덤프하기


# 샘플 색인
```
PUT cafe
{
    "mappings": {
        "menu" : {
            "properties": {
                "name" : {
                    "type": "text",
                    "index": "analyzed",
                    "term_vector": "with_positions_offsets"
                },
                "price" : {
                    "type" : "long",
                    "index" : "not_analyzed"
                }
            }
        }
    }
}

PUT cafe/menu/1
{
    "name" : "mango magic black tea",
    "price" : 1000
}

PUT cafe/menu/2
{
    "name" : "herb magic light green tea",
    "price" : 2000
}

PUT cafe/menu/3
{
    "name" : "water melon magic fresh tea",
    "price" : 3000
}
```



# span_or 검색

mango 또는 herb 토큰에 대해서 or 검색

```
GET cafe/_search
{
   "query": {
      "span_or": {
         "clauses": [
            {"span_term": {"name": "mango"}},
            {"span_term": {"name": "herb"}}
         ]
      }
   }
}
```

# span_or 검색결과
```
{
   "took": 15,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 2,
      "max_score": 1.6739764,
      "hits": [
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "1",
            "_score": 1.6739764,
            "_source": {
               "name": "mango magic black tea"
            }
         },
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "2",
            "_score": 1.6437844,
            "_source": {
               "name": "herb magic light green tea"
            }
         }
      ]
   }
}
```








