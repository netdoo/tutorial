# Elasticsearch MultiSearch Java API


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


# Elasticsearch MultiSearch Query

term 쿼리로 black을 검색하고, green을 검색함. 

```
GET cafe/_msearch
{"index":"cafe"}
{"query":{"term":{"name":{"value":"black"}}}}
{"index":"cafe"}
{"query":{"term":{"name":{"value":"green"}}}}
```


