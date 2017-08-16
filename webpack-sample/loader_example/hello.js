import Point from './point.js';
const config = require('./config.json')

module.exports = function(){
    var hello = document.createElement('div');
    hello.textContent = config.helloText + new Point(1, 23);
    return hello;
}
  