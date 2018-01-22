import Utils from './Utils.js'
import styles from './style.css';
import moment from 'moment';
import _ from 'underscore';
import {hello} from './hello.js';
import {world} from './world.js';

Utils.log(hello + ' ' + world +  ' webpack ');
document.write(moment().format('YYYY/MM/DD hh:mm:ss') + ' >> ' + _.random(0, 100));

