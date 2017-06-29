var request = require('sync-request');
var log4js = require("log4js");

log4js.configure({
  "appenders": [
    { 
        "type": "console",
        "category" : "app"
    },
    {
      "type": "file",
      "filename": "log/log_file.log",
      "maxLogSize": 20480,
      "backups": 3,
      "category": "app"
    },
    {
      "type": "file",
      "filename": "log/log_file2.log",
      "maxLogSize": 1024,
      "backups": 3,
      "category": "app"
    }
  ]
});

var logger = log4js.getLogger('app');

const host = 'http://localhost:9200';
var res;

/// 기존 car 인덱스 삭제
try {
    res = request('DELETE', host + '/car');
    logger.info(res.getBody('utf8'));
} catch (e) {
    logger.info('no such /car index');
}

/// 새로운 car 인덱스 생성
/// brand : 소문자, model : 소문자, lineup : 대문자, note : 한글
res = request('PUT', host + '/car', {
    json: {
            "settings" : {
                "index": {
                    "analysis": {
                        "analyzer": {
                            "korean": {
                                "type":"custom",
                                "tokenizer":"seunjeon_default_tokenizer"
                            },
                            "low_analyzer": {
                                "type": "custom",
                                "char_filter": ["html_strip"],
                                "filter": ["lowercase", "asciifolding"],
                                "tokenizer":"standard"
                            },
                            "upper_analyzer": {
                                "type": "custom",
                                "char_filter": ["html_strip"],
                                "filter" : ["uppercase", "asciifolding"],
                                "tokenizer" : "standard"
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
                        "model" : {"type" : "string", "analyzer" : "low_analyzer"},
                        "lineup" : {"type" : "string", "analyzer" : "upper_analyzer"},
                        "createdate" : {"type" : "date"},
                        "note" : {"type" : "string", "analyzer" : "korean"}
                    }
                }
            }
        }
});

logger.info(res.getBody('utf8'));


/// car에 적용된 analyzer 확인
res = request('GET', host + '/car/_analyze?analyzer=standard&text="Hello World"&preety');
logger.info(res.getBody('utf8'));

res = request('GET', host + '/car/_analyze?analyzer=low_analyzer&text="Hello World"&preety');
logger.info(res.getBody('utf8'));

res = request('GET', host + '/car/_analyze?analyzer=upper_analyzer&text="Hello World"&preety');
logger.info(res.getBody('utf8'));

res = request('GET', host + '/car/_analyze?analyzer=korean&text="안녕하세요. 세계입니다."&preety');
logger.info(res.getBody('utf8'));

/// car 인덱스에 샘플 데이터 입력
var reqs = {
    '/car/sales/1': {"brand": "KIA", "model": "Morning", "lineup":"compact", "createdate" : "2016-10-10", "note" : "연비가 좋은 경차"},
    '/car/sales/2': {"brand": "KIA", "model": "Sorento", "lineup":"SUV", "createdate" : "2016-10-13", "note" : "공간이 넓은 차"},
    '/car/sales/3': {"brand": "KIA", "model": "K5", "lineup":"sedan", "createdate" : "2016-10-14"},
    '/car/sales/4': {"brand": "HYUNDAI", "model": "ACCENT", "lineup":"compact", "createdate" : "2016-10-10"},
    '/car/sales/5': {"brand": "HYUNDAI", "model": "SONATA", "lineup":"sedan", "createdate" : "2016-10-10"},
    '/car/sales/6': {"brand": "HYUNDAI", "model": "SANTAFE", "lineup":"SUV", "createdate" : "2016-10-13"},
    '/car/nosales/1': {"brand": "HYUNDAI", "model": "PONY", "lineup":"compact", "createdate" : "1976-05-10"},
    '/car/nosales/2': {"brand": "DAEWOO", "model": "LEMANG", "lineup":"sedan", "createdate" : "1986-05-10"},
    '/car/nosales/3': {"brand": "KIA", "model": "BRISA", "lineup":"compact", "createdate" : "1974-10-13"}
};

for (var url in reqs) {
    res = request('POST', host+url, {
        json: reqs[url]
    });

    logger.info(res.getBody('utf8'));
}
