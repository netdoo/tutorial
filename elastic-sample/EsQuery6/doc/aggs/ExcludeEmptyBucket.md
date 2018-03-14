# 샘플 색인 작성

```
DELETE sample

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

# 샘플 데이터 입력

```
PUT sample/data/1
{
  "pid" : "A",
  "name" : "mbc"
}

PUT sample/data/2
{
  "pid" : "A",
  "name" : "mbc"
}

PUT sample/data/3
{
  "pid" : "A",
  "name" : "mbc"
}

PUT sample/data/4
{
  "pid" : "A",
  "name" : "sbs"
}

PUT sample/data/5
{
  "pid" : "A",
  "name" : "sbs"
}

PUT sample/data/6
{
  "pid" : "A",
  "name" : "kbs"
}

PUT sample/data/11
{
  "pid" : "B",
  "name" : "mbc"
}

PUT sample/data/12
{
  "pid" : "B",
  "name" : "mbc"
}

PUT sample/data/13
{
  "pid" : "B",
  "name" : "mbc"
}

PUT sample/data/14
{
  "pid" : "B",
  "name" : "sbs"
}

PUT sample/data/21
{
  "pid" : "C",
  "name" : "mbc"
}
```


# empty bucket이 포함되는 쿼리

```
GET sample/data/_search
{
  "size": 0,
  "aggs": {
    "agg_pid": {
      "terms": {
        "field": "pid",
        "size": 10
      },
      "aggs": {
        "agg_name": {
          "terms": {
            "field": "name",
            "size": 10,
            "min_doc_count": 2
          }
        }
      }
    }
  }
}
```

# empty bucket이 제외되는 쿼리

```
GET sample/data/_search
{
  "size": 0,
  "aggs": {
    "agg_pid": {
      "terms": {
        "field": "pid",
        "size": 10
      },
      "aggs": {
        "agg_name": {
          "terms": {
            "field": "name",
            "size": 10,
            "min_doc_count": 2
          }
        },
        "min_bucket_selector": {
          "bucket_selector": {
            "buckets_path": {
              "count": "agg_name._bucket_count"
            },
            "script": {
              "source": "params.count != 0"
            }
          }
        }
      }
    }
  }
}
```

# Reference 

https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-pipeline.html#buckets-path-syntax
