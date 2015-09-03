#Stratio Notebook



**v0.2.0 Changes**

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
    
--------------------------------------------------------



Interactive shell to manage **Crossdata** based on Zeppelin

Zeppelin, a web-based notebook that enables interactive data analytics. You can make beautiful data-driven, interactive and collaborative documents with SQL, Scala and more.

_Core feature_:

+ Web based notebook style editor.
+ Crossdata interpreter
+ Built-in Apache Spark support
To know more about Zeppelin, visit http://zeppelin-project.org

##Requirements

Java 1.7  
Tested on Mac OSX, CentOS 6.X, Ubuntu 14.X   
Maven (if you want to build from the source code)  
Node.js Package Manager   

##Getting Started


###Build

If you want to build Zeppelin from the source, please first clone this repository. And then:
```
mvn clean package
```

###Configure

If you wish to configure Zeppelin options (like port number), configure the following files:
```
./conf/notebook-env.sh
./conf/notebook-site.xml
```
Mesos

    # ./conf/zeppelin-env.sh
    export MASTER=mesos://...
    export NOTEBOOK_JAVA_OPTS="-Dspark.executor.uri=/path/to/spark-*.tgz" or SPARK_HOME="/path/to/spark_home"
    export MESOS_NATIVE_LIBRARY=/path/to/libmesos.so
    
If you set `SPARK_HOME`, you should deploy spark binary on the same location to all worker nodes. And if you set `spark.executor.uri`, every worker can read that file on its node.

**Crossdata files**

You can modify Crossdata driver's properties in:  
```
./conf/crossdata/driver-application.conf
```  
For using another Crossdata's version modify:   
```
./crossdata/pom.xml
```  

```
    <properties>
        <crossdata.version>HERE_YOUR_CROSSDATA'S_VERSION</crossdata.version>
    </properties>
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
Stratio Notebook allows you to save and import notebooks from a file.
For saving a Notebook to a file you just should specify the filename and it will automatically create an "export"
directory in the same path where Stratio Notebook resides. The file will have ".json" extension.
To load a Notebook from file you should indicate the full path on the system to access it.

*Be sure to have the right access and modify permissions for the user that is logged in the selected path*  

###Issues and support  
If you detect any bug or issue and need support, you can use  
[Jira](http://crossdata.atlassian.net/)
