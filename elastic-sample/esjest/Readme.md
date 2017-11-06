# Elasticsearch Jest Java API


# 샘플 색인

```
DELETE car

PUT car
{
  "settings" : {
    "index":{
      "analysis":{
        "analyzer":{
          "korean":{
            "type":"custom",
            "tokenizer":"seunjeon_default_tokenizer"
          }
        },
        "tokenizer": {
          "seunjeon_default_tokenizer": {
            "type": "seunjeon_tokenizer"
          }
        }
      }
    }
  },
  "mappings" : {
        "sales" : {
            "properties" : {
                "brand" : {"type" : "string", "index": "not_analyzed"},
                "model" : {"type" : "string", "analyzer" : "korean"},
                "lineup" : {"type" : "string", "analyzer" : "korean"},
                "createdate" : {"type" : "date"},
                "note" : {"type" : "string", "analyzer" : "korean"}
            }
        }
    }
}


PUT car/sales/1
{
    "brand": "KIA", "model": "Morning", "lineup":"compact", "createdate" : "2016-10-10", "note" : "연비가 좋은차"
}

PUT car/sales/2
{
    "brand": "KIA", "model": "Sorento", "lineup":"SUV", "createdate" : "2016-10-13", "note" : "grand car"
}

PUT car/sales/3
{
    "brand": "KIA", "model": "K5", "lineup":"sedan", "createdate" : "2016-10-14"
}

PUT car/sales/4
{
    "brand": "HYUNDAI", "model": "ACCENT", "lineup":"compact", "createdate" : "2016-10-10"
}

PUT car/sales/5
{
    "brand": "HYUNDAI", "model": "SONATA", "lineup":"sedan", "createdate" : "2016-10-10"
}

PUT car/sales/6
{
    "brand": "HYUNDAI", "model": "SANTAFE", "lineup":"SUV", "createdate" : "2016-10-13"
}


# car의 모든 문서와 필드를 출력한다.
GET car/_search?pretty

```

