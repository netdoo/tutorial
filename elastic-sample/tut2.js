
# text, keyword 타입의 차이점.

## text : full text search가 필요한 경우
## keyword : 키워드 검색, 정렬, 집계가 필요한 경우 

만약, text 필드를 기준으로, aggs 를 하게 되면, 다음과 같이 오류가 발생함.
```
 "Fielddata is disabled on text fields by default. Set fielddata=true on [title] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead."
```
 

POST /_analyze
{
    "analyzer" : "arirang_analyzer",
    "text" : "뉴욕항공권"
}



뉴욕항공권,뉴욕,항공,권으로 나뉘어짐.
{
   "tokens": [
      {
         "token": "뉴욕항공권",
         "start_offset": 0,
         "end_offset": 5,
         "type": "HANGUL",
         "position": 0
      },
      {
         "token": "뉴욕",
         "start_offset": 0,
         "end_offset": 2,
         "type": "HANGUL",
         "position": 0
      },
      {
         "token": "항공",
         "start_offset": 2,
         "end_offset": 4,
         "type": "HANGUL",
         "position": 1
      },
      {
         "token": "권",
         "start_offset": 4,
         "end_offset": 5,
         "type": "HANGUL",
         "position": 2
      }
   ]
}



DELETE bookstore

-- keyword, memo 는 검색대상에서 제외됨.
PUT bookstore
{
    "mappings": {
        "book" : {
            "properties": {
                "author" : {
                    "type": "keyword",
                    "index" : true
                },
                "nickname" : {
                    "type" : "keyword",
                    "index": false
                },
                "title" : {
                    "type": "text",
                    "index": true                    
                },
                "desc" : {
                    "type": "text",
                    "index": true,                    
                    "analyzer": "arirang_analyzer"
                },
                "memo" : {
                    "type": "text",
                    "index": false
                }
            }        
        }
    }
}




PUT bookstore/book/1
{
    "author" : "뉴욕항공권",
    "nickname" : "CEO",
    "title" : "뉴욕항공권",
    "desc" : "뉴욕항공권 이야기",
    "memo" : "뉴욕항공권 이야기"
}

-- 
GET bookstore/book/_search
{
    "query": {
        "term": {
           "author": {
              "value": "뉴욕항공권"
           }
        }
    }
}

{
   "took": 15,
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
            "_index": "bookstore",
            "_type": "book",
            "_id": "1",
            "_score": 0.2876821,
            "_source": {
               "author": "뉴욕항공권",
               "nickname": "CEO",
               "title": "뉴욕항공권",
               "desc": "뉴욕항공권 이야기",
               "memo": "뉴욕항공권 이야기"
            }
         }
      ]
   }
}

--
-- "Cannot search on field [nickname] since it is not indexed."
GET bookstore/book/_search
{
    "query": {
        "term": {
           "nickname": {
              "value": "CEO"
           }
        }
    }
}


--
GET bookstore/book/_search
{
    "query": {
        "term": {
           "desc": {
              "value": "뉴욕"
           }
        }
    }
}

{
   "took": 25,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 1,
      "max_score": 0.3037427,
      "hits": [
         {
            "_index": "bookstore",
            "_type": "book",
            "_id": "1",
            "_score": 0.3037427,
            "_source": {
               "author": "뉴욕항공권",
               "nickname": "CEO",
               "title": "뉴욕항공권",
               "desc": "뉴욕항공권 이야기",
               "memo": "뉴욕항공권 이야기"
            }
         }
      ]
   }
}


--
GET bookstore/book/_search
{
    "query": {
        "term": {
           "desc": {
              "value": "항공"
           }
        }
    }
}

{
   "took": 25,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 1,
      "max_score": 0.3037427,
      "hits": [
         {
            "_index": "bookstore",
            "_type": "book",
            "_id": "1",
            "_score": 0.3037427,
            "_source": {
               "author": "뉴욕항공권",
               "nickname": "CEO",
               "title": "뉴욕항공권",
               "desc": "뉴욕항공권 이야기",
               "memo": "뉴욕항공권 이야기"
            }
         }
      ]
   }
}


--
--  "Cannot search on field [memo] since it is not indexed."
GET bookstore/book/_search
{
    "query": {
        "wildcard": {
           "memo": {
              "value": "*항공권"
           }
        }
    }
}


--
GET bookstore/book/_search
{
    "query": {
        "span_term": {
           "desc": {
              "value": "항공"
           }
        }
    }
}


{
   "took": 10,
   "timed_out": false,
   "_shards": {
      "total": 5,
      "successful": 5,
      "failed": 0
   },
   "hits": {
      "total": 1,
      "max_score": 0.3037427,
      "hits": [
         {
            "_index": "bookstore",
            "_type": "book",
            "_id": "1",
            "_score": 0.3037427,
            "_source": {
               "author": "뉴욕항공권",
               "nickname": "CEO",
               "title": "뉴욕항공권",
               "desc": "뉴욕항공권 이야기",
               "memo": "뉴욕항공권 이야기"
            }
         }
      ]
   }
}


--

GET bookstore/book/_search 
{
    "query": {
        "term": {
           "title": {
              "value": "뉴욕"
           }
        }
    }
}

검색 결과 없음.








