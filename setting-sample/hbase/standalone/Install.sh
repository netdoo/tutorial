Standalone HBase

# wget http://apache.mirror.cdnetworks.com/hbase/1.3.0/hbase-1.3.0-bin.tar.gz
# tar xvfz hbase-1.3.0-bin.tar.gz
# cd hbase-1.3.0

# vi /etc/profile

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export HBASE_HOME=/root/hbase-1.3.0

# vi conf/hbase-site.xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///home/testuser/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/home/testuser/zookeeper</value>
  </property>
</configuration>

# bin/start-hbase.sh

# jps
2250 Jps
1883 HMaster

# bin/hbase shell


hbase(main):001:0> create 'test', 'cf'
0 row(s) in 2.0690 seconds

=> Hbase::Table - test


hbase(main):002:0> list 'test'
TABLE
test
1 row(s) in 0.0330 seconds

=> ["test"]


hbase(main):003:0> put 'test', 'row1', 'cf:a', 'value1'
0 row(s) in 0.3500 seconds

hbase(main):004:0> put 'test', 'row2', 'cf:b', 'value2'
0 row(s) in 0.0570 seconds

hbase(main):005:0> put 'test', 'row3', 'cf:c', 'value3'
0 row(s) in 0.0240 seconds

hbase(main):006:0> scan 'test'
ROW                   COLUMN+CELL
 row1                 column=cf:a, timestamp=1492769322835, value=value1
 row2                 column=cf:b, timestamp=1492769331098, value=value2
 row3                 column=cf:c, timestamp=1492769335981, value=value3
3 row(s) in 0.0630 seconds


hbase(main):008:0> get 'test', 'row1'
COLUMN                CELL
 cf:a                 timestamp=1492769322835, value=value1
1 row(s) in 0.0270 seconds


hbase(main):008:0> disable 'test'
0 row(s) in 1.1820 seconds

hbase(main):009:0> enable 'test'
0 row(s) in 0.1770 seconds

hbase(main):010:0> drop 'test'
0 row(s) in 1.3030 seconds


hbase(main):011:0> quit

# bin/stop-hbase.sh
root@mynode1:~/hbase-1.3.0# bin/stop-hbase.sh
stopping hbase.....................

