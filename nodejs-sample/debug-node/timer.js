var someStopFlag = 0;

function printTime() {

    if (1 == someStopFlag) {
        console.log('someStopFlag == 1');
        stopTimer();
    }
    
    console.log(new Date().toLocaleTimeString());
}

localTimer = setInterval(printTime, 1000);
           
/// 50초 이후에, stopTimer함수가 호출됨.
setTimeout(stopTimer, 500000);

function stopTimer() {
    clearInterval(localTimer);
    console.log('stopTimer()');
    process.exit(0);
}