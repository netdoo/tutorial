
# kia, hyundai 색인 생성            
```
PUT kia
PUT hyundai
```

# 추가된 색인 확인
```
GET _cat/indices
```

# 샘플 데이터 입력 
```
PUT kia/car/1
{
    "name" : "pride"
}

PUT kia/car/2
{
    "name" : "morning"
}

PUT kia/car/3
{
    "name" : "sorento"
}

PUT hyundai/car/1
{
    "name" : "accent"
}

PUT hyundai/car/2
{
    "name" : "sonata"
}

PUT hyundai/car/3
{
    "name" : "santafe"
}
```


# 샘플 데이터 입력 확인
```
GET _search
{
    "query": { "match_all": {} }
}
```

## 결과 
```
pride, morning, sorento, accent, sonata, santafe
```


# 알리어스 추가 

kia, hyundai 색인이 auto 라는 알리어스로 묶임.

```
POST _aliases
{
    "actions": [
       {
          "add": {
             "index": "kia",
             "alias": "auto"
          }
       },
       {
           "add": {
               "index": "hyundai",
               "alias": "auto"
           }
       }
    ]
}
```

# 알리어스 검색 
```
GET auto/_search
```

## 결과 
```
pride, morning, sorento, accent, sonata, santafe 
```

# 알리어스 수정

기존에 kia, hyundai로 묶인 alias중에서
hyundai 색인만 제거함.

```
POST _aliases
{
    "actions": [
       {
          "remove": {
             "index": "hyundai",
             "alias": "auto"
          }
       }
    ]
}
```

```
GET auto/_search
```

## 결과 
```
pride, morning, sorento 
```

# 알리어스에 Filter 조건 추가
데이터베이스의 View 처럼, Alias에도 선택적인 데이터 추출을 위한
필터 적용이 가능하다.
```
POST _aliases
{
    "actions": [
       {
          "add" : {
             "index": "kia",
             "alias": "auto",
             "filter": {
                 "query": {
                     "wildcard": {
                        "name": "*or*"
                    }
                 }
             }
          }
       }
    ]
}
```

```
GET auto/_search
```

## 결과 

```
morning, sorento 
```

