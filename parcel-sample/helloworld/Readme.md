# Parcel 설치

```
npm install -g parcel-bundler
yarn global add parcel-bundler
```


# 샘플 파일 준비


## 1. index.html 
```
<html>
<head>
</head>
<body class="main">
  <script src="./index.js"></script>
</body>
</html>
```

## 2. index.js
```
import main from './main';

main.writeLog('hello parcel');


```

## 3. main.js 
```
// CSS 모듈 임포트
import classes from './main.css';

class Main {
    static writeLog(msg) { 
        console.log('[LOG] ' + msg) 
    }
}

export default Main;



```

## 4. main.css
```
.main {
  /* 이미지 파일 참조 */
  background: url('images/bg.jpg');
  color: red;
}
```


# 작업환경
```
parcel index.html -p 1111
```

# 배포환경
```
parcel build index.js -d build/output --no-minify --no-cache
```
