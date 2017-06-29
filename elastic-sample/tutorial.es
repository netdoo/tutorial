

elasticsearch-2.4.0 설치 


cd C:\Tool\elastic\elasticsearch-2.4.0
bin\plugin.bat remove hq
bin\plugin.bat remove head
bin\plugin.bat remove analysis-seunjeon

bin\plugin.bat install royrusso/elasticsearch-HQ
bin\plugin.bat install mobz/elasticsearch-head
bin\plugin.bat install org.bitbucket.eunjeon/elasticsearch-analysis-seunjeon/2.4.0.0

http://bakyeono.net/post/2016-08-20-elasticsearch-querydsl-basic.html

bin\elasticsearch.bat 
cd C:\Project\elastic-sample

curl -X DELETE localhost:9200/car
curl -X PUT localhost:9200/car --data @scheme.json -H "Content-Type: application/json"
curl -X PUT localhost:9200/car/sales/1 --data @data1.json -H "Content-Type: application/json"







#############

curl -X DELETE localhost:9200/car

curl -X PUT localhost:9200/car -d ' 
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
}'


curl -X PUT localhost:9200/car/sales/1 -d '{"brand": "KIA", "model": "Morning", "lineup":"compact", "createdate" : "2016-10-10", "note" : "연비가 좋은차"}'

curl -X PUT localhost:9200/car/sales/2 -d '{"brand": "KIA", "model": "Sorento", "lineup":"SUV", "createdate" : "2016-10-13", "note" : "grand car"}'

curl -X PUT localhost:9200/car/sales/3 -d '{"brand": "KIA", "model": "K5", "lineup":"sedan", "createdate" : "2016-10-14"}'

curl -X PUT localhost:9200/car/sales/4 -d '{"brand": "HYUNDAI", "model": "ACCENT", "lineup":"compact", "createdate" : "2016-10-10"}'

curl -X PUT localhost:9200/car/sales/5 -d '{"brand": "HYUNDAI", "model": "SONATA", "lineup":"sedan", "createdate" : "2016-10-10"}'

curl -X PUT localhost:9200/car/sales/6 -d '{"brand": "HYUNDAI", "model": "SANTAFE", "lineup":"SUV", "createdate" : "2016-10-13"}'

curl -X PUT localhost:9200/car/nosales/1 -d '{"brand": "HYUNDAI", "model": "PONY", "lineup":"compact", "createdate" : "1976-05-10"}'

curl -X PUT localhost:9200/car/nosales/2 -d '{"brand": "DAEWOO", "model": "LEMANG", "lineup":"sedan", "createdate" : "1986-05-10"}'

curl -X PUT localhost:9200/car/nosales/3 -d '{"brand": "KIA", "model": "BRISA", "lineup":"compact", "createdate" : "1974-10-13"}'



# car의 모든 문서와 필드를 출력한다.
curl -XGET localhost:9200/car/_search?pretty

# car의 1번 문서를 출력한다.
curl -XGET localhost:9200/car/sales/1?pretty


# car의 모든 문서를 검색하고, brand와 model 필드만을 출력한다.
curl -XGET localhost:9200/car/_search?pretty -d '
{
    "fields" : ["brand", "model"]
}' 

# sales의 모든 문서를 검색하고, brand와 model 필드만을 출력한다.
curl -XGET localhost:9200/car/sales/_search?pretty -d '
{
    "fields" : ["brand", "model"]
}' 

# nosales의 모든 문서를 검색하고, brand와 model 필드만을 출력한다.
curl -XGET localhost:9200/car/nosales/_search?pretty -d '
{
    "query": {"match_all": {}}, 
    "fields" : ["brand", "model"]
}'


# sales의 brand가 kia인 문서를 검색하고, brand와 model 필드만을 출력한다.
curl -XGET localhost:9200/car/sales/_search?pretty -d '
{
    "query": {
        "match": {
            "brand":"kia"
            }
        }, 
    "fields" : ["brand", "model"]
}'

# sales의 model이 *or*인 문서를 검색하고, brand와 model 필드만을 출력한다. (느린검색)
curl -XGET localhost:9200/car/sales/_search?pretty -d '
{
    "query": {
        "wildcard": {
            "model":"*or*"
            }
        }, 
    "fields" : ["brand", "model"]
}'

# sales의 brand prefix가 ki인 문서를 검색하고, brand와 model 필드만을 출력한다. (느린검색)
curl -XGET localhost:9200/car/sales/_search?pretty -d '{
    "query": {
        "prefix": {
            "brand":"ki"
            }
        }, 
    "fields" : ["brand", "model"]
}'




# sales의 brand가 kia인 문서를 검색하고, 검색 결과중 1번째 부터, 1개의 문서를 가져와서 brand와 model 필드만을 출력한다.
curl -XGET localhost:9200/car/sales/_search?pretty -d '
{
    "from":1, 
    "size":1, 
    "query": {
        "match": {
            "brand":"kia"
            }
        }, 
    "fields" : ["brand", "model"]
}'


# sales의 brand가 kia인 문서를 검색하고, 검색 결과를 createdate 필드 기준으로 오름차순으로 정렬하고 brand와 model 필드만을 출력한다.
curl -XPOST localhost:9200/car/sales/_search?pretty -d '{
    "sort": [
        {"createdate": {"order": "asc"}}, 
        "_score"
        ], 
    "query": {"match": {"brand":"kia"}}, 
    "fields" : ["brand", "model", "createdate"]
}'






# note 에 '연비'가 포함된 문서를 검색
curl -XGET localhost:9200/car/_search?q=note:'연비' 

# note 에 '좋은차'가 포함된 문서를 검색
curl -XGET localhost:9200/car/_search?q=note:'좋은차'

# car에서 2016년 10월 1일 부터 2016년 10월 14일까지 생산된 차를, 생산일자 기준 오름차순으로 출력함.
curl -XGET localhost:9200/car/_search?pretty -d '
{
  "query": {
    "filtered": {
      "filter": {
        "range": {
          "createdate": {
            "gte": "2016-10-01",
            "lt": "2016-10-14"
          }
        }
      }
    }
  },
  "sort": [{"createdate": {"order": "asc"}}, 
        "_score"
        ]
}'

# car에서 모델이 K5인 차량을 검색.
curl -XPOST localhost:9200/car/sales/_search?preety -d '
{
    "query" : {
        "bool" : {
            "should" : {
                "term" : {
                    "model" : "k5"
                }
            }
        }
    }
}'


# car 에서 brand가 hyundai 인 차량을 검색.

curl -XPOST localhost:9200/_search?preety -d '
{
  "query": {
    "term": {
      "brand" : "hyundai"
    }
  }
}'



# car에서 PONY를 제외한 brand가 HYUNDAI 차량을 검색

curl -XGET localhost:9200/_search?preety -d '
{
  "query": {
    "filtered": {
      "query": {
        "match_all": {}
      },
      "filter": {
        "bool": {
          "must": [
            {"term": {"brand": "hyundai"}}
          ],
          "should": [
            {"term": {"brand": "hyundai"}}
          ],
          "must_not": [
            {"term": {"model": "pony"}}
          ]
        }
      }
    }
  }
}'


# car에서 brand가 HYUNDAI인 차량을 검색.
curl -XGET localhost:9200/_search?preety -d '
{
  "query": {
    "filtered": { 
      "filter": {
        "term": {
            "brand": "hyundai",
            "_cache": "true"
        }   
      }
    }
  }
}'


# car/sales 에서 brand가 HYUNDAI인 차량을 검색. 

curl -XGET localhost:9200/car/sales/_search?pretty -d '
{
    "query": {
        "match": {"brand":"HYUNDAI"}
    }, 
    "fields" : ["brand", "model"]
}'


# brand 별로, 등록된 차량수를 조회

curl -XPOST localhost:9200/car/_search?preety -d '
{
  "aggs": {
    "group_by_brand": {
      "terms": {"field": "brand"}
    }
  }
}'




