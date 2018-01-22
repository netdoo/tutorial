import Utils from './Utils.js'
import styles from './style.css';
import moment from 'moment';
import _ from 'underscore';

Utils.log('Hello webpack 123')
document.write(moment().format('YYYY/MM/DD hh:mm:ss') + ' >> ' + _.random(0, 100));

