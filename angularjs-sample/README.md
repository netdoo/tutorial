# angularjs-sample


## 정의
    Angular는 자바스크립트로 만든 client 측 MVC/MVVM 프레임워크로 
    모던 단일 페이지 웹 애플리케이션(혹은 웹사이트) 개발시에 사용이 된다.


## 순서
    1. helloworld.html
    2. directive.html 
    3. service.html 
    4. filter.html 
    5. databind.html 
    6. http 
    7. scope
    8. express 


## 예제
```javascript
<!DOCTYPE html>
<html ng-app="myApp">
<head>
<title>Hello World, AngularJS</title>
<meta charset="utf-8">
<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.17/angular.min.js"></script>
<script type="text/javascript">
    var myApp = angular.module('myApp', []);
    
    /// 컨트롤러는 JSON 데이터로 서버와 통신하는 함수(이벤트 함수도!)와 데이터 만을 다룬다.
    myApp.controller('MainCtrl', ['$scope', function ($scope) {
    
        /// $scope 는 DOM의 현재 요소/영역을 참조하며(this 와는 다르다), 
        /// 요소안의 데이터와 로직을 주시하는 아주 멋진 관찰 기능을 가지고 있다. 
    
        $scope.text = 'Hello World';
        $scope.user = {};
        $scope.user.details = {
            "username" : "Tomas",
            "id" : "89101112"
        }
    }]);
</script>
</head>
<body>
    <div ng-app="myApp">
        <div ng-controller="MainCtrl">
            <p>{{ text }}</p>
            <p>{{ user.details.username }}</p>
            <p>{{ user.details.id }}</p>
        </div>
    </div>
</body>
</html>
```
