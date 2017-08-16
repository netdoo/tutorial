
# webpack 정리

1. javascript 언어를 지원하는 모듈 시스템 
2. webpack은 프로젝트의 구조를 분석하여 브라우저에 적용가능한 번들로 묶고 패킹하는 기능을 제공함.


# webpack 설치


```
# npm install -g webpack
```


# main.js

```js
var hello = require('./hello.js');
document.getElementById('root').appendChild(hello());
```


# hello.js

```js
module.exports = function() {
    var hello = document.createElement('div');
    hello.textContent = "hello webpack";
    return hello;
}  
```


# index.html

```html
<!DOCTYPE html>
<html leng="en">
<head>
  <meta charset="utf-8" />
    <title>WebPack Sample</title>
</head>
<body>
    <div id='root'>
    
  </div>
    <script src = 'bundle.js'></script>
</body>
</html>
```

# webpack 실행

```
$ webpack main.js bundle.js
```



# webpack.config.js 구성 파일 정의

webpack.config.js

```js
module.exports = {
entry:__dirname + "/main.js",
output: {
    path: __dirname + "/",
    filename: "bundle.js"
    }
}
```


# webpack.config.js 파일 기반의 webpack 실행

webpack.config.js 파일이 있기 때문에, 
webpack만 실행하면 된다.


```sh
$ webpack 
```
















# webpack json-loader 정리

1. json 파일을 분석 및 활용 할 때 사용


# webpack json-loader 설치

```sh
# npm install --save-dev json-loader
```

# webpack.config.js 파일에 json-loader 추가

```js
module.exports = {
    entry:__dirname + "/main.js",
    output: {
        path: __dirname + "/",
        filename: "bundle.js"
    },
    module:{ 
        loaders : [
          {
            test: /\.json$/,
            loader: 'json-loader'
          }
        ]
    }
}
```

# config.json

```json
{
  "helloText":"Hello World"
}
```

# hello.js 

```js
const config = require('./config.json')

module.exports = function(){
    var hello = document.createElement('div');
    hello.textContent = config.helloText;
    return hello;
}
```

# webpack 실행

```sh
$ webpack
```



























# Babel 정의

ES6 기능을 제공하는 라이브러리


# Babel 설치 

```sh
$ npm install --save-dev babel-core babel-loader babel-preset-es2015 babel-preset-react
```




# Babel 활성화 

webpack.config.js 파일에 babel-loader 추가함.

```js
module.exports = {
    entry:__dirname + "/main.js",
    output: {
        path: __dirname + "/",
        filename: "bundle.js"
    },
    module:{ 
        loaders : [
          {
            test: /\.json$/,
            loader: 'json-loader'
          },
          {  
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'babel-loader', 
            query: {
                presets:['es2015','react']
                }
            }
        ]
    }
}
```


# point.js

```js
class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
    toString() {
        return '('+this.x+','+this.y+')';
    }
}
export default Point;
```

# hello.js

```js
import Point from './point.js';
const config = require('./config.json')

module.exports = function(){
    var hello = document.createElement('div');
    hello.textContent = config.helloText + new Point(1, 23);
    return hello;
}
```

# webpack 실행

```sh
$ webpack
```





