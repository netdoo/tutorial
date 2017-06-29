
var log4js = require("log4js");
var request = require('sync-request');
const host = 'http://localhost:9200';
var logger = log4js.getLogger('app');
var util = require('./util.js');
var res;

res = request('GET', host + '/car/_search');
logger.info('%s',  util.grepResult("car의 모든 문서와 필드를 출력한다.", res));


res = request('GET', host + '/car/sales/1');
logger.info('%s', util.grepResult("car의 1번 문서를 출력한다.", res));


res = request('GET', host + '/car/sales/123');
logger.info('%s', util.grepResult("car의 123번 문서를 출력한다.", res));

res = request('POST', host + '/car/_search', {
    json : {
        "fields" : ["brand", "model"]
    }
});

logger.info('%s', util.grepResult("car의 모든 문서를 검색하고, brand와 model 필드만을 출력한다.", res));


res = request('POST', host + '/car/sales/_search?pretty', {
    json : {
        "fields" : ["brand", "model"]
    }
});

logger.info('%s', util.grepResult("sales의 모든 문서를 검색하고, brand와 model 필드만을 출력한다.", res));


res = request('POST', host + '/car/nosales/_search?pretty', {
    json : {
        "query": {"match_all": {}}, 
        "fields" : ["brand", "model"]
    }
});

logger.info('%s', util.grepResult("nosales의 모든 문서를 검색하고, brand와 model 필드만을 출력한다.", res));


/// brand 필드에는 not_analyzed 색인이 적용되어 있기 때문에,
/// match 쿼리 사용시 대소문자를 구분하여 정확히 입력해야 한다.
/// "brand" : {"type" : "string", "index": "not_analyzed"},
res = request('POST', host + '/car/sales/_search?pretty', {
    json : {
        "query": {
            "match": {
                "brand":"KIA"
                }
            }, 
        "fields" : ["brand", "model"]
    }
});

logger.info('%s', util.grepResult('sales의 brand가 kia인 문서를 검색하고, brand와 model 필드만을 출력한다.', res));


res = request('POST', host + '/car/sales/_search?pretty', {
    json : {
        "query": {
            "wildcard": {
                "model":"*or*"
                }
            }, 
        "fields" : ["brand", "model"]
    }
});

logger.info('%s', util.grepResult('sales의 model이 *or*인 문서를 검색하고, brand와 model 필드만을 출력한다. (느린검색)', res));


res = request('POST', host + '/car/sales/_search?pretty', {
    json : {
        "query": {
            "prefix": {
                "brand":"KI"
                }
            }, 
        "fields" : ["brand", "model"]
    }
});

logger.info('%s', util.grepResult('sales의 brand prefix가 ki인 문서를 검색하고, brand와 model 필드만을 출력한다. (느린검색)', res));


res = request('POST', host + '/car/sales/_search?pretty', {
    json : {
        "from":1, 
        "size":1, 
        "query": {
            "match": {
                "brand":"KIA"
                }
            }, 
        "fields" : ["brand", "model"]
    }    
});

logger.info('%s', util.grepResult('sales의 brand가 kia인 문서를 검색하고, 검색 결과중 1번째 부터, 1개의 문서를 가져와서 brand와 model 필드만을 출력한다.', res));


res = request('POST', host + '/car/sales/_search?pretty', {
    json : {
        "sort": [
            {"createdate": {"order": "asc"}}, 
            "_score"
            ], 
        "query": {"match": {"brand":"KIA"}}, 
        "fields" : ["brand", "model", "createdate"]
    }
});

logger.info('%s', util.grepResult('sales의 brand가 kia인 문서를 검색하고, 검색 결과를 createdate 필드 기준으로 오름차순으로 정렬하고 brand와 model 필드만을 출력한다.', res));


res = request('POST', host+'/car/_search?preety', {
    json: {
        "query": {
            "match": {
                "note": "연비 공간"
            }
        }, 
        "fields" : ["brand", "model", "note"]
    }
});

logger.info('%s', util.grepResult('note에 연비 또는 공간이라고 기록된 문서를 검색함.', res));


var res = request('POST', host+'/car/_search?preety', {
    json: {
        "query": {
            "dis_max" : {
                "queries" : [
                    {"match" : {"note" : "연비"}},
                    {"match" : {"note" : "공간"}}
                ]
            }
        },
        "fields" : ["brand", "model", "note"]
    }
});

logger.info('%s', util.grepResult('note에 연비 또는 공간이라고 기록된 문서를 검색함.', res));


res = request('POST', host+'/car/_search?preety', {
    json: {
            "query": {
                "bool": {
                    "should": [{"match": {"note": "연비"}}, {"match": {"note": "공간"}}]
                }
            }
        }
});

logger.info('%s', util.grepResult('note에 연비 또는 공간이라고 기록된 문서를 검색함.', res));


res = request('POST', host+'/car/_search?preety', {
    json : {
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
        "sort": [
            {"createdate": {"order": "asc"}}, 
            "_score"
        ]
    }
});

logger.info('%s', util.grepResult('car에서 2016년 10월 1일 부터 2016년 10월 14일까지 생산된 차를, 생산일자 기준 오름차순으로 출력함.', res));


res = request('POST', host+'/car/_search?preety', {
    json : {
        "query" : {
            "bool" : {
                "should" : {
                    "term" : {
                        "model" : "k5"
                    }
                }
            }
        }
    }
});

logger.info(util.grepResult('car에서 모델이 K5인 차량을 검색.', res));


res = request('POST', host+'/car/_search?preety', {
    json : {
        "query": {
            "term": {
                "brand" : "HYUNDAI"
            }
        }
    }
});

logger.info(util.grepResult('car 에서 brand가 hyundai 인 차량을 검색.', res));



res = request('POST', host + '/car/_search', {
    json : {
        "query": {
            "filtered": {
                "query": {
                    "match_all": {}
                },
                "filter": {
                    "bool": {
                        "must": [
                            {"term": {"brand": "HYUNDAI"}}
                        ],
                        "should": [
                            {"term": {"brand": "HYUNDAI"}}
                        ],
                        "must_not": [
                            {"term": {"model": "pony"}}
                        ]
                    }
                }
            }
        }
    }
});

logger.info(util.grepResult('car에서 PONY를 제외한 brand가 HYUNDAI 차량을 검색', res));


res = request('POST', host + '/car/_search', {
    json : {
        "query": {
            "filtered": { 
                "filter": {
                    "term": {
                        "brand": "HYUNDAI",
                        "_cache": "true"
                    }   
                }
            }
        }
    }
});

logger.info(util.grepResult('car에서 brand가 HYUNDAI인 차량을 검색.', res));



res = request('POST', host + '/car/sales/_search', {
    json : {
        "query": {
            "match": {"brand":"HYUNDAI"}
        }, 
        "fields" : ["brand", "model"]
    }
});

logger.info(util.grepResult('car/sales 에서 brand가 HYUNDAI인 차량을 검색. ', res));

res = request('POST', host + '/car/sales/_search', {
    json : {
        "aggs": {
            "group_by_brand": {
                "terms": {"field": "brand"}
            }
        }
    }
});

logger.info(util.grepAggResult('brand 별로, 등록된 차량수를 조회', res));

