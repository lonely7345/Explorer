=====================
How do I get started?
=====================

Requirements
------------

| Java 1.7
| Tested on Mac OSX, CentOS 6.X, Ubuntu 14.X
| Maven (if you want to build from the source code)
| Node.js Package Manager Apache Cassandra DataBase Running(if you want
  to use Apache Cassandra Interpreter) Stratio Crossdata Running (If you
  want to use Stratio Crossdata Interpreter) Apache Spark node Running
  (If you want to use Apache Spark and Apache Spark-Sql interpreter)

Getting Started
---------------

Install
~~~~~

Explorer is packed in a rpm and y a deb file. If you want install Stratio Explorer choses the correct file for your operating symen and install it.


Run
~~~

::

    ./bin/explorer.sh -> to launch a console session depending instance

or

::

    ./bin/explorer-daemon.sh start -> to launch the service

The default URL is:

::

    http:\\<server>:8084

Since there is no real auth system yet, you have to use dummy
credentials to enter:

::

    username : Stratio
    password : Stratio

Stop
~~~~

::

    ./bin/explorer-daemon.sh stop

browse localhost:8084 in your browser. 8085 port should be accessible
for websocket connection. **For configuration details check ./conf
subdirectory.**