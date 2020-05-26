#!/usr/bin/env bash
if [[ $1 == "start" || $1 == "restart" ]]; then
    ID=`ps -ef | grep "backup.war" | grep -v "$0" | grep -v "grep" | awk '{print $2}'`
        echo $ID
        for id in $ID
        do
        kill -9 $id
        echo "killed $id"
        done
        #nohup java -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=10.40.17.35 -server -Xmx5g -Xms5g -XX:+PrintGCDetails -jar cmadaas-sod-backup.war  --server.port=8080 >/space/cmadaas/sod/sod-backclear-logs/gc.log 2>&1 &
nohup java -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=10.40.17.35 -server -Xms200g -Xmx200g -Xss512k -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=45 -XX:NewRatio=2 -XX:SurvivorRatio=6 -XX:G1ReservePercent=10 -XX:G1HeapRegionSize=32m -XX:ConcGCThreads=32 -Xloggc:/space/cmadaas/sod/logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -jar ./cmadaas-sod-backup.war  --server.port=8058 >/dev/null 2>&1 &
        echo "启动成功"

elif [ $1 == "stop" ]; then
    ID=`ps -ef | grep "backup.war" | grep -v "$0" | grep -v "grep" | awk '{print $2}'`
        echo $ID
        for id in $ID
        do
        kill -9 $id
        echo "killed $id"
        done
        echo "停止成功"

elif [ $1 == "status" ]; then
 ps -ef|grep backup.war
else
    echo "请输入参数start|status|stop|restart"
fi
