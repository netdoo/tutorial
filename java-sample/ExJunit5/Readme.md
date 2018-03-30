# SomeTest 클래스의 모든 테스트를 실행

```sh
$ mvn test -Dtest=SomeTest
```

# SomeTest 클래스의 fooTest 만 테스트 함.

```sh
$ mvn test -Dtest=SomeTest#fooTest
````


# SomeTest 클래스의 *Test와 매칭되는 테스트만 테스트 함.

```sh
$ mvn test -Dtest=SomeTest#*Test
```

