# create index

```json
PUT autocomplete_test
{
  "settings" : {
    "index" : {
      "analysis" : {
        "analyzer" : {
          "autocomplete_analyzer" : {
            "type" : "custom",
            "tokenizer" : "lowercase",
            "filter"    : ["asciifolding", "title_ngram"]
          }
        },
        "filter" : {
          "title_ngram" : {
            "type" : "nGram",
            "min_gram" : 3,
            "max_gram" : 5
          }
        }
      }
    }
  },
  "mappings": {
    "city": {
      "properties": {
        "city": {
          "type": "string",
          "analyzer": "autocomplete_analyzer",
          "boost": 10
        }
      }
    }
  }
}
```

```json
GET autocomplete_test/_analyze 
{
    "analyzer" : "autocomplete_analyzer",
    "text" : "Ouderkerk+aan+de+Amstel"
}
```



# Post some sample documents

```json
POST autocomplete_test/city
{ 
    "city" : "Amsterdam" 
}

POST autocomplete_test/city
{ 
    "city" : "Amstelveen"     
}

POST autocomplete_test/city
{ 
    "city" : "Ouderkerk aan de Amstel"     
}

POST autocomplete_test/city
{ 
    "city" : "Alphen aan den Rijn"     
}

POST autocomplete_test/city
{ 
    "city" : "Den Haag" 
}

POST autocomplete_test/city
{ 
    "city" : "Rotterdam"    
}

POST autocomplete_test/city
{ 
    "city" : "Groningen"     
}

POST autocomplete_test/city
{ 
    "city" : "Castelré" 
}

POST autocomplete_test/city
{ 
    "city" : "Etten-Leur"     
}

POST autocomplete_test/city
{ 
    "city" : "Babyloniënbroek" 
}
```



# Make sure everything is indexed

```json
POST autocomplete_test/_refresh
```


# Test Suggest Autocomplete

```json
GET autocomplete_test/_search 
{
    "query": {
        "match_all": {}
    }
}

GET autocomplete_test/_search
{
    "query": {
        "match": {
           "city" : "Ams"
        }
    }
}

```











