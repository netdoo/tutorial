pm2 start app.js -i 4   # 4개의 프로세스로 시작.
pm2 scale app 5         # 프로세스를 5개로 늘림.
pm2 scale app 3         # 프로세스를 3개로 줄임.
pm2 logs app            # app의 로그를 출력함.
pm2 restart app         # app을 재시작함.
pm2 stop app            # app을 종료함.
 