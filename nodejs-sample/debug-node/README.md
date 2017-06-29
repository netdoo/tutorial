



> node --inspect --debug-brk timer.js
<code>
Debugger listening on port 9229.
Warning: This is an experimental feature and could change at any time.
To start debugging, open the following URL in Chrome:
    chrome-devtools://devtools/remote/serve_file/@60cd6e859b9f557d2312f5bf532f6aec5f284980/inspector
.html?experiments=true&v8only=true&ws=127.0.0.1:9229/f2a15512-a1af-4cd4-a370-c45b2726b815
</code>





node-inspector 설치 
<code>
시작 > 실행 > cmd > npm install -g node-inspector 
</code>

디버깅 모드로 node 실행 
<code>
시작 > 실행 > cmd > node --debug timer.js 
</code>

node-inspector 실행 
<code>
시작 > 실행 > cmd > node-inspector 
</code>

크롬을 사용하여, node-inspector에 접속 
시작 > 실행 > 크롬 실행 > http://127.0.0.1:8080/?ws=127.0.0.1:8080&port=5858 접속 


이 후, 크롬에서 디버깅을 수행함. 
