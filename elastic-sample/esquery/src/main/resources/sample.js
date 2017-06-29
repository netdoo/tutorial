DELETE bool

PUT bool
{
    "mappings": {
        "sample" : {
            "properties": {
                "title" : {"type":"text"},
                "author" : {"type": "text"},
                "category" : {"type": "text"},
                "studio" : {"type": "text"},
                "movie_type" : {"type": "text"}
            }
        }
    }
}

PUT bool/sample/1
{
    "title" : "x-man",
    "author" : "marvel",
    "category" : "action",
    "studio" : "hollywood",
    "movie_type" : "2D"
}

PUT bool/sample/2
{
    "title" : "superman",
    "author" : "dc comics",
    "category" : "action",
    "studio" : "hollywood",
    "movie_type" : "2D"
}

PUT bool/sample/3
{
    "title" : "superman returns",
    "author" : "dc comics",
    "category" : "action",
    "studio" : "hollywood",
    "movie_type" : "3D"
}

PUT bool/sample/4
{
    "title" : "superman",
    "author" : "dc comics",
    "category" : "action",
    "studio" : "hollywood",
    "movie_type" : "3D"
}

PUT bool/sample/5
{
    "title" : "superman",
    "author" : "marvel",
    "category" : "action",
    "studio" : "hollywood",
    "movie_type" : "3D"
}

POST bool/_flush

POST bool/sample/_search

POST bool/sample/_search
{
  "query": {
    "bool" : {
      "must" : [
          {"term" : { "studio" : "hollywood" }  },
          {"term" : { "title" : "superman" }  }
        ],
        "must_not": [
           {"term": {"movie_type": "3D" } }
        ]
    }
  }
}

POST bool/sample/_search
{
   "query": {
      "bool": {
      "must" : [
          {"term" : { "studio" : "hollywood" } },
          {"term" : { "title" : "superman" } },
          {"term" : { "movie_type" : "3d"} }
        ]
      }
   }
}


POST bool/sample/_search
{
   "query": {
      "bool": {
      "must" : [
          {"term" : { "studio" : "hollywood" } },
          {"term" : { "title" : "superman" } },
          {"term" : { "movie_type" : "3d"} }
        ],
        "must_not": [
           {"term": {  "author": "marvel" } }
        ]
      }
   }
}
