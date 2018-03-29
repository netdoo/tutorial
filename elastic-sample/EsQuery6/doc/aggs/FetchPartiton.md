# 샘플 색인 생성

```concept
PUT partition_sample
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

```concept
PUT partition_sample/data/1
{
  "pid" : "A",
  "name" : "MBC"
}

PUT partition_sample/data/2
{
  "pid" : "A",
  "name" : "MBC"
}

PUT partition_sample/data/3
{
  "pid" : "B",
  "name" : "KBS"
}

PUT partition_sample/data/4
{
  "pid" : "B",
  "name" : "KBS"
}

PUT partition_sample/data/5
{
  "pid" : "C",
  "name" : "SBS"
}

PUT partition_sample/data/6
{
  "pid" : "C",
  "name" : "SBS"
}

PUT partition_sample/data/7
{
  "pid" : "D",
  "name" : "EBS"
}

PUT partition_sample/data/8
{
  "pid" : "D",
  "name" : "EBS"
}

PUT partition_sample/data/9
{
  "pid" : "E",
  "name" : "TVN"
}

PUT partition_sample/data/10
{
  "pid" : "E",
  "name" : "TVN"
}

PUT partition_sample/data/11
{
  "pid" : "F",
  "name" : "JTBC"
}
```

# PID 기준으로 집계

```concept
GET partition_sample/data/_search
{
  "size": 0,
  "aggs": {
    "agg_pid": {
      "terms": {
        "field": "pid",
        "size": 10
      }
    }
  }
}
```

# PID 기준으로 집계한 결과
```concept
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 11,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "agg_pid": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "A",
          "doc_count": 2
        },
        {
          "key": "B",
          "doc_count": 2
        },
        {
          "key": "C",
          "doc_count": 2
        },
        {
          "key": "D",
          "doc_count": 2
        },
        {
          "key": "E",
          "doc_count": 2
        },
        {
          "key": "F",
          "doc_count": 1
        }
      ]
    }
  }
}
```



# PID 기준으로 파티셔닝 집계한 결과중 0번째 파티션

- partition : 현재 보고자 하는 파티션 번호
- num_partitions : 전체 파티션 갯수
- size : 집계하고자 하는 건수

예를 들어 전체 집계하고자 하는 건수가 10 이고, 총 파티션을 3이라고 가정하면, 
다음과 같이 파티션 번호를 각각 0, 1, 2로 지정하여 조회하면 됩니다.

```concept
GET partition_sample/data/_search
{
  "size": 0,
  "aggs": {
    "agg_pid": {
      "terms": {
        "field": "pid",
        "include": {
          "partition" : 0,
          "num_partitions" : 3
        },
        "size": 10
      }
    }
  }
}
```


# PID 기준으로 파티셔닝 집계한 결과중 0번째 파티션 실행결과
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
    "total": 11,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "agg_pid": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "B",
          "doc_count": 2
        }
      ]
    }
  }
}
```


# PID 기준으로 파티셔닝 집계한 결과중 1번째 파티션
```concept
GET partition_sample/data/_search
{
  "size": 0,
  "aggs": {
    "agg_pid": {
      "terms": {
        "field": "pid",
        "include": {
          "partition" : 1,
          "num_partitions" : 3
        },
        "size": 10
      }
    }
  }
}
```

# PID 기준으로 파티셔닝 집계한 결과중 1번째 파티션 실행결과
```concept
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 11,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "agg_pid": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "A",
          "doc_count": 2
        },
        {
          "key": "F",
          "doc_count": 1
        }
      ]
    }
  }
}
```

# PID 기준으로 파티셔닝 집계한 결과중 2번째 파티션
```concept
GET partition_sample/data/_search
{
  "size": 0,
  "aggs": {
    "agg_pid": {
      "terms": {
        "field": "pid",
        "include": {
          "partition" : 2,
          "num_partitions" : 3
        },
        "size": 10
      }
    }
  }
}
```


# PID 기준으로 파티셔닝 집계한 결과중 2번째 파티션 실행결과
```concept
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 11,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "agg_pid": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "C",
          "doc_count": 2
        },
        {
          "key": "D",
          "doc_count": 2
        },
        {
          "key": "E",
          "doc_count": 2
        }
      ]
    }
  }
}
```

