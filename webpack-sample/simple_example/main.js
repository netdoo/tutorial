import {hello} from './hello.js';
import {world} from './world.js';
import Utils from './Utils.js'
import foo from './foo.js';

Utils.log(hello + ' ' + world +  ' webpack ');
document.write(hello + ' ' + world +  ' webpack ');

// bundle.js 에 사용된 함수를 외부로 export 하기 위해서 사용함.
window.foo = foo;
