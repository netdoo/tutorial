
const config = require('./config.json')

module.exports = function(){
    var hello = document.createElement('div');
    hello.textContent = config.helloText;
    return hello;
}
  