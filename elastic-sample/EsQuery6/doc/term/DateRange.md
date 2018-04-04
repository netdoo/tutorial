
# 기존 색인 삭제
```
DELETE color 
```

# 샘플 색인 생성
```
PUT color
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "data" : {
      "dynamic" : "strict",
      "properties": {
        "name" : {
          "type": "keyword",
          "index": true
        },
        "createDate" : {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss.SSS",
          "index": true
        }
      }
    }
  }
}
```


# 샘플 색인 데이터 입력
```
POST color/data/1
{
  "name" : "red",
  "createDate" : "2018-03-01 10:10:10.290"
}

POST color/data/2
{
  "name" : "green",
  "createDate" : "2018-03-02 10:10:10.290"
}

POST color/data/3
{
  "name" : "blue",
  "createDate" : "2018-03-03 10:10:10.290"
}

POST color/data/4
{
  "name" : "white",
  "createDate" : "2018-03-03 12:10:10.190"
}
```


# 날짜 조회
```
GET color/data/_search
{
  "query": {
    "range": {
      "createDate": {
        "from": "2018-03-01 10:10:10.290",
        "to": "2018-03-03 10:10:10.290"
      }
    }
  }
}
```

# 날짜 조회 결과
```
{
  "took": 68,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 3,
    "max_score": 1,
    "hits": [
      {
        "_index": "color",
        "_type": "data",
        "_id": "1",
        "_score": 1,
        "_source": {
          "name": "red",
          "createDate": "2018-03-01 10:10:10.290"
        }
      },
      {
        "_index": "color",
        "_type": "data",
        "_id": "2",
        "_score": 1,
        "_source": {
          "name": "green",
          "createDate": "2018-03-02 10:10:10.290"
        }
      },
      {
        "_index": "color",
        "_type": "data",
        "_id": "3",
        "_score": 1,
        "_source": {
          "name": "blue",
          "createDate": "2018-03-03 10:10:10.290"
        }
      }
    ]
  }
}
```

# 날짜를 내림차순으로 정렬하여 상위 1개만 조회
```
GET color/data/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "createDate": {
            "from": "2018-03-01 10:10:10.290",
            "to": "2018-03-03 10:10:10.290"
          }
        }
      },
      "boost": 1.2
    }
  },
  "sort": [
    {
      "createDate": {
        "order": "desc"
      }
    }
  ],
  "size": 1,
  "_source": [
    "name",
    "createDate"
  ]
}
```

# 날짜를 내림차순으로 정렬하여 상위 1개만 조회 결과
```
{
  "took": 6,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 3,
    "max_score": null,
    "hits": [
      {
        "_index": "color",
        "_type": "data",
        "_id": "3",
        "_score": null,
        "_source": {
          "name": "blue",
          "createDate": "2018-03-03 10:10:10.290"
        },
        "sort": [
          1520071810290
        ]
      }
    ]
  }
}
```


# 3월 1일 ~ 3월 2일 사이의 데이터를 내림차순으로 상위 20개만 조회

```
GET color/data/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "createDate": {
            "from": "2018-03-01",
            "to": "2018-03-02",
            "format": "yyyy-MM-dd"
          }
        }
      },
      "boost": 1.2
    }
  },
  "sort": [
    {
      "createDate": {
        "order": "desc"
      }
    }
  ],
  "size": 20,
  "_source": [
    "name",
    "createDate"
  ]
}
```



# 3월 1일 ~ 3월 2일 사이의 데이터를 내림차순으로 상위 20개만 조회 결과

```
{
  "took": 7,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": null,
    "hits": [
      {
        "_index": "color",
        "_type": "data",
        "_id": "2",
        "_score": null,
        "_source": {
          "name": "green",
          "createDate": "2018-03-02 10:10:10.290"
        },
        "sort": [
          1519985410290
        ]
      },
      {
        "_index": "color",
        "_type": "data",
        "_id": "1",
        "_score": null,
        "_source": {
          "name": "red",
          "createDate": "2018-03-01 10:10:10.290"
        },
        "sort": [
          1519899010290
        ]
      }
    ]
  }
}
```

