"""
requests lib 설치 
$ pip install requests
"""

import threading
import requests

class WorkClient(threading.Thread):
    def run(self):
        for _ in range(10):
            response = requests.get('http://api.ipify.org?format=json')
            data = response.json()
            lock = threading.Lock()
            lock.acquire()
            print ("%s %s" %(threading.currentThread().getName(),data))
            lock.release()

def thread_example():
    work1 = WorkClient(name = 'work1')
    work2 = WorkClient(name = 'work2')

    work1.start()
    work2.start()

    work1.join()
    work2.join()


def simple_example():
    # 인증이 필요한 경우.
    # response = requests.get('http://api.ipify.org?format=json', auth=('user', 'password'))
    response = requests.get('http://api.ipify.org?format=json')
    data = response.json()
    print (data)



simple_example()
thread_example()


