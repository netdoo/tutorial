# Elasticsearch 검색 결과를 CSV 로 덤프하기


# 샘플 색인
```
PUT cafe
{
    "mappings": {
        "menu" : {
            "properties": {
                "name" : {
                    "type": "string",
                    "index": "analyzed",
                    "term_vector": "with_positions_offsets"
                },
                "price" : {
                    "type" : "long",
                    "index" : "not_analyzed"
                },
                "comment"  : {
                    "type" : "nested",
                    "properties" : {
                        "taste" : {"type" : "string"},
                        "season" : {"type" : "string"},
                        "takeout" : {"type" : "boolean"}
                    }
                },
                "extra" : {
                    "properties": {
                        "size" : {"type": "string"},
                        "sizeup" : {"type": "boolean"}
                    }
                }
            }
        }
    }
}

PUT cafe/menu/1
{
    "name" : "mango magic black tea",
    "price" : 1000,
    "comment" : {
        "taste" : "sweet",
        "season" : "summer",
        "takeout" : true
    },
    "extra" : {
        "size" : "normal",
        "sizeup" : false
    }
}

PUT cafe/menu/2
{
    "name" : "herb magic light green tea",
    "price" : 2000,
    "comment" : {
        "taste" : "good sweet",
        "season" : "summer",
        "takeout" : true
    },
    "extra" : {
        "size" : "small",
        "sizeup" : false
    }
}

PUT cafe/menu/3
{
    "name" : "water melon magic fresh tea",
    "price" : 3000,
    "comment" : {
        "taste" : "sweet",
        "season" : "spring",
        "takeout" : false
    },
    "extra" : {
        "size" : "big",
        "sizeup" : true
    }
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
   "took": 122,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 2,
      "max_score": 0.9521713,
      "hits": [
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "1",
            "_score": 0.9521713,
            "_source": {
               "name": "mango magic black tea",
               "price": 1000,
               "comment": {
                  "taste": "sweet",
                  "season": "summer",
                  "takeout": true
               },
               "extra": {
                  "size": "normal",
                  "sizeup": false
               }
            }
         },
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "2",
            "_score": 0.8331499,
            "_source": {
               "name": "herb magic light green tea",
               "price": 2000,
               "comment": {
                  "taste": "good sweet",
                  "season": "summer",
                  "takeout": true
               },
               "extra": {
                  "size": "small",
                  "sizeup": false
               }
            }
         }
      ]
   }
}
```








