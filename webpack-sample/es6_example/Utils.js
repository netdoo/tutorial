import moment from 'moment';

class Utils {
    static log(msg) { 
        console.log('[LOG] ' + moment().format('YYYY/MM/DD hh:mm:ss') + ' ' + msg) 
    }
}

export default Utils;
