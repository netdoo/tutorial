
# strict 매핑

## strict 샘플색인 생성
```concept

PUT food
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "data": {
      "dynamic" : "strict",
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

```concept
{
  "acknowledged": true,
  "shards_acknowledged": true,
  "index": "food"
}
```


## strict 샘플색인 데이터 입력 #1
```concept
PUT food/data/1 
{
  "pid" : "A",
  "name" : "egg"
}
```
## strict 샘플색인 데이터 입력 #1 실행결과
```concept
{
  "_index": "food",
  "_type": "data",
  "_id": "1",
  "_version": 1,
  "result": "created",
  "_shards": {
    "total": 1,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 0,
  "_primary_term": 1
}
```

## strict 샘플색인 데이터 입력 #2
```concept
PUT food/data/2
{
  "pid" : "A",
  "name" : "egg",
  "price" : 1000
}
```

## strict 샘플색인 데이터 입력 #2 실행결과
> price 필드가 mapping에 없기 때문에, 예외가 발생함.
```concept
{
  "error": {
    "root_cause": [
      {
        "type": "strict_dynamic_mapping_exception",
        "reason": "mapping set to strict, dynamic introduction of [price] within [data] is not allowed"
      }
    ],
    "type": "strict_dynamic_mapping_exception",
    "reason": "mapping set to strict, dynamic introduction of [price] within [data] is not allowed"
  },
  "status": 400
}
```

# dynamic 매핑


```concept
PUT food2
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "data": {
      "dynamic" : "true",
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

```concept
{
  "acknowledged": true,
  "shards_acknowledged": true,
  "index": "food2"
}
```


```concept

PUT food2/data/1 
{
  "pid" : "A",
  "name" : "egg"
}

```

```concept
{
  "_index": "food2",
  "_type": "data",
  "_id": "1",
  "_version": 1,
  "result": "created",
  "_shards": {
    "total": 1,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 0,
  "_primary_term": 1
}
```

```concept

PUT food2/data/2
{
  "pid" : "A",
  "name" : "egg",
  "price" : 1000
}
```

```concept
{
  "_index": "food2",
  "_type": "data",
  "_id": "2",
  "_version": 1,
  "result": "created",
  "_shards": {
    "total": 1,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 1,
  "_primary_term": 1
}
```


```concept
GET food2/_search
```
 
```concept
{
  "took": 0,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1,
    "hits": [
      {
        "_index": "food2",
        "_type": "data",
        "_id": "1",
        "_score": 1,
        "_source": {
          "pid": "A",
          "name": "egg"
        }
      },
      {
        "_index": "food2",
        "_type": "data",
        "_id": "2",
        "_score": 1,
        "_source": {
          "pid": "A",
          "name": "egg",
          "price": 1000
        }
      }
    ]
  }
}
```