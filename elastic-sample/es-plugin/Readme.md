
# Elasticsearch 5.1.2 기반의 플러그인 개발하기



* Project name : es-plugin
* Project location : C:\Project\es-plugin

```sh
$ mvn archetype:generate -DgroupId=com.esplugin -DartifactId=es-plugin 
  -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

IntelliJ 등을 사용하여 필요한 부분을 구현하고,

$ mvn clean package 

$ bin/elasticsearch-plugin install file:///C:/Project/es-plugin/target/releases/es-plugin-1.0.zip
-> Downloading file:///C:/Project/es-plugin/target/releases/es-plugin-1.0.zip
[=================================================] 100%??
-> Installed es-plugin

$ bin\elasticsearch-plugin list
es-plugin

$ bin\elasticsearch.bat  
[2017-04-27T11:54:07,761][WARN ][n.g.e.p.b.BasicPlugin    ] Create the Basic Plugin and installed it into elasticsearch
[2017-04-27T11:54:07,905][INFO ][c.e.ThePlugin            ] Create The Plugin
[2017-04-27T11:54:07,917][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [aggs-matrix-stats]
[2017-04-27T11:54:08,020][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [ingest-common]
[2017-04-27T11:54:08,033][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [lang-expression]
[2017-04-27T11:54:08,044][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [lang-groovy]
[2017-04-27T11:54:08,092][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [lang-mustache]
[2017-04-27T11:54:08,109][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [lang-painless]
[2017-04-27T11:54:08,115][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [percolator]
[2017-04-27T11:54:08,121][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [reindex]
[2017-04-27T11:54:08,141][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [transport-netty3]
[2017-04-27T11:54:08,152][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded module [transport-netty4]
[2017-04-27T11:54:08,155][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded plugin [elastic-basic-plugin]
[2017-04-27T11:54:08,185][INFO ][o.e.p.PluginsService     ] [Qiv1z3r] loaded plugin [es-plugin]

```

# 테스트 색인 생성

```
PUT the
{
   "settings": {
      "index": {
         "analysis": {
            "analyzer": {
               "the_analyzer": {
                  "type": "custom",
                  "tokenizer": "standard",
                  "filter": [
                     "the_filter"
                  ]
               }
            },
            "filter": {
               "the_filter": {
                  "type": "es-filter"
               }
            }
         }
      }
   },
   "mappings": {
      "user": {
         "properties": {
            "name": {
               "type": "text",
               "index": "analyzed",
               "analyzer": "the_analyzer"
            }
         }
      }
   }
}
```


# 애널라이저 확인

```
GET _analyze
{
  "tokenizer" : "standard",
  "filter" : ["es-filter"],
  "text" : "this is a test for google"
}


GET _analyze
{
  "tokenizer" : "standard",
  "filter" : ["es-filter"],
  "text" : "abc"
}
```


# 샘플 데이터 입력

```
PUT the/user/1
{
    "name" : "this is test for google"
}
```


# 전체 데이터 확인
```
GET _search
```

# google로 검색
```
GET the/_search
{ 
    "query": {
        "match": {
           "name": "google"
        }
    }
}

{
   "took": 20,
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
            "_index": "the",
            "_type": "user",
            "_id": "1",
            "_score": 0.2876821,
            "_source": {
               "name": "this is test for google"
            }
         }
      ]
   }
}
```

# abc로 검색

```
GET the/_search
{ 
    "query": {
        "match": {
           "name": "abc"
        }
    }
}

{
   "took": 18,
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
            "_index": "the",
            "_type": "user",
            "_id": "1",
            "_score": 0.2876821,
            "_source": {
               "name": "this is test for google"
            }
         }
      ]
   }
}
```

# REST API 테스트
```
root@mynode1:~# curl -XGET '192.168.56.1:9200/_the?pretty'
{
  "message" : "default action"
}

root@mynode1:~# curl -XGET '192.168.56.1:9200/_the/sub?pretty'
{
  "message" : "your action is sub"
}
```

