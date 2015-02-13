#Stratio Crossdata ishell


Interactive shell to manage **Crossdata based** on Zeppelin

Zeppelin, a web-based notebook that enables interactive data analytics. You can make beautiful data-driven, interactive and collaborative documents with SQL, Scala and more.

_Core feature_:

*Web based notebook style editor.
*Crossdata interpreter
*Built-in Apache Spark support
To know more about Zeppelin, visit our web site http://zeppelin-project.org

##Requirements


Java 1.7
Tested on Mac OSX, CentOS 6.X
Maven (if you want to build from the source code)

##Getting Started


###Build

If you want to build Zeppelin from the source, please first clone this repository. And then:

`mvn clean package`

Build with specific version

`mvn clean package -Dspark.version=1.1.1 -Dhadoop.version=2.0.0-mr1-cdh4.6.0`

###Configure

If you wish to configure Zeppelin option (like port number), configure the following files:

`./conf/zeppelin-env.sh
./conf/zeppelin-site.xml`

**Run**

`bin/zeppelin.sh` -> to launch a console session depending instance 
`./bin/zeppelin-daemon.sh start` -> to launch the demo

**Stop**

`bin/zeppelin-daemo.sh stop`

browse localhost:8080 in your browser. 8081 port should be accessible for websocket connection.
**For configuration details check ./conf subdirectory.**
