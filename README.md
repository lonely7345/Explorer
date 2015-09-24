#Stratio Explorer



**v0.4.0 Changes**

--------------------------------------------------------

-Run paragraph force output visualization   
-Notebook’s loading delay minimized  
    +Font load improvement  
    +Websocket call control  
-Update to Spark 1.4.0   
-Connection to remote Spark cluster  
-Stratio Streaming intepreter completed  
    +Customized parser for streaming shell syntax  
    +Syntax highlighting and help command   
    +Syntax autocomplete helper 
-Add Cassandra Interpreter   
-Add Stratio Ingestion interpreter   
-Add Stratio Ingestion configurable settings (in view)   
-Add Stratio Cassandra configurable settings (in view)   
    
--------------------------------------------------------



Interactive shell to manage **Crossdata,Spark,Spark-sql,Ingestion,MarkDown,Shell,Streaming,Cassandra** based on Zeppelin

Zeppelin, a web-based notebook that enables interactive data analytics. You can make beautiful data-driven, interactive and collaborative documents with SQL, Scala and more.

_Core feature_:

+ Web based notebook style editor.
+ Stratio Crossdata interpreter
+ Apache Spark Interpreter
+ Apache Spark-SQL interpreter
+ Stratio Ingestion Interpreter
+ Markdown Interpreter
+ Shell Interpreter
+ Stratio Streaming Interpreter
+ Apache Cassandra interpreter
+ Built-in Apache Spark support
To know more about Explorer , visit http://docs.stratio.com/

##Requirements

Java 1.7  
Tested on Mac OSX, CentOS 6.X, Ubuntu 14.X   
Maven (if you want to build from the source code)  
Node.js Package Manager
Apache Cassandra DataBase Running(if you want to use Apache Cassandra Interpreter)
Stratio Crossdata Running (If you want to use Stratio Crossdata Interpreter)
Apache Spark node Running (If you want to use Apache Spark and Apache Spark-Sql interpreter)

##Getting Started


###Build

If you want to build Explorer from the source, please first clone this repository. And then:
```
mvn clean package
```

###Configure

If you wish to configure Explorer options (like port number), configure the following files:
```
./conf/notebook-env.sh
./conf/notebook-site.xml
```
If you want to configure Apache Mesos you must uncomment this lines of :

    # ./conf/notebook-env.sh
    export MASTER=mesos://...
    export NOTEBOOK_JAVA_OPTS="-Dspark.executor.uri=/path/to/spark-*.tgz" or SPARK_HOME="/path/to/spark_home"
    export MESOS_NATIVE_LIBRARY=/path/to/libmesos.so
    
If you set `SPARK_HOME`, you should deploy spark binary on the same location to all worker nodes. And if you set `spark.executor.uri`, every worker can read that file on its node.

**Stratio Crossdata configuration files**

You can modify Stratio Crossdata driver's properties in:
```
./conf/crossdata/driver-application.conf
```  
For using another Stratio Crossdata's version modify:
```
./crossdata/pom.xml
```  

```
    <properties>
        <crossdata.version>HERE_YOUR_CROSSDATA'S_VERSION</crossdata.version>
    </properties>
```  

**Stratio Ingestion configuration files**

You can modify Stratio Ingestion driver's properties in:

```
./conf/ingestions.conf
```
**Apache Cassandra configuration files**

```
./conf/cassandra.conf
```
###Run
```
./bin/notebook.sh -> to launch a console session depending instance 
```  
or   
```
./bin/notebook-daemon.sh start -> to launch the service
```  

The default URL is:
```
http:\\<server>:8084
```
Since there is no real auth system yet, you have to use dummy credentials to enter:

```
username : Stratio
password : Stratio
```

###Stop
```
./bin/notebook-daemon.sh stop
```

browse localhost:8084 in your browser. 8085 port should be accessible for websocket connection.
**For configuration details check ./conf subdirectory.**

###Save
Stratio Explorer allows you to save and import notebooks from a file.
For saving a Notebook to a file you just should specify the filename and it will automatically create an "export"
directory in the same path where Stratio Notebook resides. The file will have ".json" extension.
To load a Notebook from file you should indicate the full path on the system to access it.

*Be sure to have the right access and modify permissions for the user that is logged in the selected path*  

###Issues and support  
If you detect any bug or issue and need support, you can use  
[Jira](http://crossdata.atlassian.net/)
