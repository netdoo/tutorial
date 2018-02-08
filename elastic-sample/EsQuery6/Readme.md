



# install elastic search 6.x 

```concept
$ curl -L -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.1.3.tar.gz
$ tar xvfz elasticsearch-6.1.3.tar.gz
```

# start elastic search 6.x 

```
$ cd elasticsearch-6.1.3
$ bin/elasticsearch
```

# install kibana 

```
$ curl -L -O https://artifacts.elastic.co/downloads/kibana/kibana-6.1.3-windows-x86_64.zip
$ 7z -x kibana-6.1.3-windows-x86_64.zip
```

# start kibana

```
$ cd kibana\bin 
$ bin\kibana.bat
```

# browse kibana

```
http://localhost:5601/app/kibana#/dev_tools/console?_g=()
```





# Elasticsearch Span 쿼리 종류


* span_first : end값 이전까지 token이 포함되어 있는지 검색
* span_or : or 형태의 token을 검색
* span_near : token과 token 사이의 끼인 toke의 허용갯수를 고려한 검색



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


# span_first 검색 

black 토큰이 3번째(end) 이전에 위치한 경우 검색하는 쿼리
```
GET cafe/_search
{
    "query": {
        "span_first": {
           "match": {
              "span_term": {"name": "black"}
           },
           "end": 3
        }
    }
}
```

# span_first 검색결과
```
{
   "took": 13,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 1,
      "max_score": 0.2876821,
      "hits": [
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "1",
            "_score": 0.2876821,
            "_source": {
               "name": "mango magic black tea"
            }
         }
      ]
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


# span_near 검색 

magic 토큰과 tea 토큰사이에 
최대 1개(slop)까지의 잘못된 토큰만 허용하여 검색

```
GET cafe/_search
{
   "query": {
      "span_near": {
         "clauses": [                     
               {"span_term": {"name": "magic"}},
               {"span_term": {"name": "tea"}}              
         ],
         "slop": 1,
         "in_order": false
      }
   }
}
```

# span_near 검색결과
```
{
   "took": 6,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 2,
      "max_score": 0.27517417,
      "hits": [
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "1",
            "_score": 0.27517417,
            "_source": {
               "name": "mango magic black tea"
            }
         },
         {
            "_index": "cafe",
            "_type": "menu",
            "_id": "3",
            "_score": 0.26810864,
            "_source": {
               "name": "water melon magic fresh tea"
            }
         }
      ]
   }
}
```







# Reference

```concept
http://essql.nlpcn.org/
```

```concept
SELECT count(*),avg(balance) FROM bank
```


```concept
{
	"from": 0,
	"size": 0,
	"_source": {
		"includes": [
			"COUNT",
			"AVG"
		],
		"excludes": []
	},
	"aggregations": {
		"COUNT(*)": {
			"value_count": {
				"field": "_index"
			}
		},
		"AVG(balance)": {
			"avg": {
				"field": "balance"
			}
		}
	}
}

```


# 쿼리 사용전 주의사항

1. 쿼리를 사용하기 위해서는 색인되어 있어야 함. (index : true)


# match 쿼리

1. 대소문자 구분안함.


# bool 쿼리

## 나이가 40세이지만 ID(아이다호)에 살고 있지 않은 사람의 모든 계정을 반환합니다.

```concept
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "age": "40" } }
      ],
      "must_not": [
        { "match": { "state": "ID" } }
      ]
    }
  }
}
```

# Elasticsearch date type 

```concept
PUT my_index
{
  "mappings": {
    "_doc": {
      "properties": {
        "date": {
          "type":   "date",
          "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
        }
      }
    }
  }
}

PUT my_index/_doc/1
{ "date": "2015-01-01" } 

PUT my_index/_doc/2
{ "date": "2015-01-01T12:10:30Z" } 

PUT my_index/_doc/3
{ "date": 1420070400001 } 

GET my_index/_search
{
  "sort": { "date": "asc"} 
}
```
