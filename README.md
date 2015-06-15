#Stratio Notebook

Interactive shell to manage **Crossdata** based on Zeppelin

Zeppelin, a web-based notebook that enables interactive data analytics. You can make beautiful data-driven, interactive and collaborative documents with SQL, Scala and more.

_Core feature_:

+ Web based notebook style editor.
+ Crossdata interpreter
+ Built-in Apache Spark support
To know more about Zeppelin, visit http://zeppelin-project.org

##Requirements

Java 1.7
Tested on Mac OSX, CentOS 6.X
Maven (if you want to build from the source code)

##Getting Started


###Build

If you want to build Zeppelin from the source, please first clone this repository. And then:
```
mvn clean package
```

###Configure

If you wish to configure Zeppelin option (like port number), configure the following files:
```
./conf/zeppelin-env.sh
./conf/zeppelin-site.xml
```
**Crossdata files**

You can modify Crossdata driver properties in:
```
./conf/crossdata/driver-application.conf
```


###Run
```
./bin/zeppelin.sh -> to launch a console session depending instance 
./bin/zeppelin-daemon.sh start -> to launch the service
```
Since there is no real auth system yet, you have to use dummy credentials to enter:

username : test
password : test

###Stop
```
./bin/zeppelin-daemon.sh stop
```

browse localhost:8080 in your browser. 8081 port should be accessible for websocket connection.
**For configuration details check ./conf subdirectory.**
