# npm install
# node app.js
# curl http://127.0.0.1:3000?name=james

[Server]

> node app.js
[2017-02-20 15:56:59.036] [INFO] app - Example app listening on port 3000!
[2017-02-20 15:57:06.961] [INFO] app - visit james
[2017-02-20 15:57:06.974] [INFO] app - ::ffff:127.0.0.1 - - "GET /?name=james HTTP/1.1" 200 11 "" "curl/7.45.0"

[Client]
> curl http://127.0.0.1:3000?name=james
Hello james

