# CSS 캐쉬방지 방법 1

```
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
```

* Expires : 0초 이후, 본 문서가 유효하지 않음을 의미함.







# CSS 캐쉬방지 방법 2

```
<link rel="stylesheet" href="/path/to/css/my.css?20180410">
<script type="text/javascript" src="/path/to/js/my.js?20180410"></script>
```



# <주의사항>

* IE 5.0 등 오래된 브라우저에서는 캐쉬방지가 오동작 할 수 있음.

