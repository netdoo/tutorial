
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
