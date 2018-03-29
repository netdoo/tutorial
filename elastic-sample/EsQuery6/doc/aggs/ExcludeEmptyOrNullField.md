
# 샘플색인 
```concept
PUT sample
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "data": {
      "properties": {
        "pid": {
          "type": "keyword",
          "index": true
        },
        "name": {
          "type": "keyword",
          "index": true
        }
      }
    }
  }
}
```

# 샘플데이터 입력
```concept
PUT sample/data/1
{
  "pid" : "A",
  "name" : "mbc"
}

PUT sample/data/2
{
  "pid" : "B",
  "name" : ""
}

PUT sample/data/3
{
  "pid" : "C"
}

PUT sample/data/4
{
  "pid" : "D",
  "name" : "sbs"
}
```

# 입력된 데이터 전체 확인
```concept
GET sample/_search
```

```concept
{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 1,
    "hits": [
      {
        "_index": "sample",
        "_type": "data",
        "_id": "1",
        "_score": 1,
        "_source": {
          "pid": "A",
          "name": "mbc"
        }
      },
      {
        "_index": "sample",
        "_type": "data",
        "_id": "2",
        "_score": 1,
        "_source": {
          "pid": "B",
          "name": ""
        }
      },
      {
        "_index": "sample",
        "_type": "data",
        "_id": "3",
        "_score": 1,
        "_source": {
          "pid": "C"
        }
      },
      {
        "_index": "sample",
        "_type": "data",
        "_id": "4",
        "_score": 1,
        "_source": {
          "pid": "D",
          "name": "sbs"
        }
      }
    ]
  }
}
```


# 공백또는 NULL 을 제거한 결과만 조회

- exists : 지정된 필드에 값이 있는 경우만 검색 대상에 포함.
- term : 지정된 필드에 지정된 값이 정확히 일치하는 경우를 찾기 위해 사용함.

```concept
GET sample/data/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "bool": {
          "must": [
            {
              "exists": {
                "field": "name"
              }
            }
          ],
          "must_not": [
            {
              "term": {
                "name": {
                  "value": ""
                }
              }
            }
          ]
        }
      },
      "boost": 1.2
    }
  },
  "size": 20
}
```

```concept
{
  "took": 4,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.2,
    "hits": [
      {
        "_index": "sample",
        "_type": "data",
        "_id": "1",
        "_score": 1.2,
        "_source": {
          "pid": "A",
          "name": "mbc"
        }
      },
      {
        "_index": "sample",
        "_type": "data",
        "_id": "4",
        "_score": 1.2,
        "_source": {
          "pid": "D",
          "name": "sbs"
        }
      }
    ]
  }
}
```