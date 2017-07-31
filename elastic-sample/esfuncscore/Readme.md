# Elasticsearch Function Score 


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
                }
            }
        }
    }
}

PUT cafe/menu/1
{
    "name" : "mango magic black tea"
}

PUT cafe/menu/2
{
    "name" : "herb magic light green tea"
}

PUT cafe/menu/3
{
    "name" : "water melon magic fresh tea"
}
```


# 

모든 검색 결과의 score는 5가 됨. 

```
GET cafe/menu/_search 
{
   "query": {
      "function_score": {
         "query": {
            "match_all": {}
         },
         "boost": "5",
         "functions": []
      }
   }
}
```

# Elastcisearch Function Score Boost Mode 

검색결과에 green 토큰이 포함되어 있는 경우, 스코어에 23을 multiply 해줌. 

```
GET cafe/menu/_search 
{
   "query": {
      "function_score": {
         "query": {
            "match_all": {}
         },
         "functions": [
            {
               "filter": {
                  "term": {
                     "name": "green"
                  }
               },
               "weight": 23
            }
         ],
         "boost": "5",
         "boost_mode": "multiply",
         "max_boost": 100
      }
   }
}
```
