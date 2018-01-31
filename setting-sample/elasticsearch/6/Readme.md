
# install elastic search 6.x 

```
$ curl -L -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.1.3.tar.gz
$ tar xvfz elasticsearch-6.1.3.tar.gz
```

# start elastic search 6.x 

```
$ cd elasticsearch-6.1.3
$ bin/elasticsearch
```

# install kibana 

```
$ curl -L -O https://artifacts.elastic.co/downloads/kibana/kibana-6.1.3-windows-x86_64.zip
$ 7z -x kibana-6.1.3-windows-x86_64.zip
```

# start kibana

```
$ cd kibana\bin 
$ bin\kibana.bat
```

# browse kibana

```
http://localhost:5601/app/kibana#/dev_tools/console?_g=()
```


