"use strict";

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

class Parent {
    constructor(){
        this.a = 1;

        /// private variable 는 상속되지 않는다.
        let _code = 111;

        this.setCode = function(code) {
            _code = code;
        }

        this.getCode = function() {
            return _code;
        }
    }

    static staticMethod() {
        this.s = 11;
        return 'parent static method';
    }

    say() {
        return 'parent say';
    }
}

class Child extends Parent {
    constructor(){
        super();
        this.b = 2;


    }

    static staticMethod() {
        this.s = 11;
        return 'child static method';
    }

    say() {
        return 'child say';
    }

    supersay() {
        return super.say();
    }

    /// getCode와 setCode 함수는 상속되나,
    /// _code 변수는 상속되지 않기 때문에, getCode2() 함수 호출시
    /// ReferenceError: _code is not defined 예외가 발생한다.
    getCode2() {
        return _code;
    }
}

const obj = new Child();

console.log(obj.a, obj.b);                  /// 1 2
console.log(obj.hasOwnProperty('a'));       /// true
console.log(obj.hasOwnProperty('b'));       /// true
console.log(Parent.staticMethod());         /// arent static method
console.log(Child.staticMethod());          /// child static method
console.log(obj.say());                     /// child say
console.log(obj.supersay());                /// parent say
console.log(obj._code);                     /// undefined
console.log(obj.getCode());                 /// 111
obj.setCode(2222);
console.log(obj.getCode());                 /// 2222
console.log(obj.hasOwnProperty('_code'));   /// false

logger.info('exit');
