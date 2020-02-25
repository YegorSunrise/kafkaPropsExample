start cmd.exe /C C:\apache-zookeeper-3.5.6\bin\zkServer
timeout 10
start cmd.exe /C C:\apache-zookeeper-3.5.6\bin\zkCli
timeout 5
start cmd.exe /C c:\kafka_2.12-2.4.0\bin\windows\kafka-server-start.bat C:\kafka_2.12-2.4.0\config\server-0.properties
timeout 5
start cmd.exe /C c:\kafka_2.12-2.4.0\bin\windows\kafka-server-start.bat C:\kafka_2.12-2.4.0\config\server-1.properties
timeout 5
start cmd.exe /C c:\kafka_2.12-2.4.0\bin\windows\kafka-server-start.bat C:\kafka_2.12-2.4.0\config\server-2.properties