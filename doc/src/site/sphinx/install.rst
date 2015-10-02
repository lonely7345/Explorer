=================
Install Explorer
=================

Instructions
^^^^^^^^^^^^^



  You can access Explorer with browser http://localhost:8084

Install
^^^^^^^
Configuring Explorer with Maven

Prerequisites
-------------
Java 1.7 or Later
Apache Maven 
Node.js (to use npm)

Install
-------
To install Stratio Explorer you must follow this Steps.

1.- Clone or download project from https://github.com/Stratio/Explorer
2.- From Shell move to Explorer folder
3.- Excute maven package

.. code-block::bash
  
  mvn clean package package 

Configure
---------
Configuration can be done by both environment variable and java properties. If both defined, environment variable is
used.

=========================    =======================  ============================== ===========
explorer-env.sh	             explorer-site.xml         Default value  		     Description
=========================    =======================  ============================== ===========
EXPLORER_HOME	  		    		   	   		   	     Explorer Home directory
EXPLORER_PORT         	     zeppelin.server.port     8084	   		     Explorer server port
EXPLORER_JOB_DIR             zeppelin.job.dir         jobs	   		     Explorer persist/load session in this directory. Can be a path or a URI. location on HDFS supported
EXPLORER_ZAN_REPO            zeppelin.zan.repo        https://github.com/NFLabs/zan  Remote ZAN repository URL
EXPLORER_ZAN_LOCAL_REPO      zeppelin.zan.localrepo   zan-repo	 		     Explorer library local repository. Local filesystem path
EXPLORER_ZAN_SHARED_REPO     zeppelin.zan.sharedrepo				     Explorer library shared repository. Location on HDFS. Usufull when your backend (eg. hiveserver) is not running on the sam machine and want to use zeppelin library with resource file(eg. in hive 'ADD FILE 'path'). So your backend can get resource file from shared repository.
EXPLORER_DRIVERS             zeppelin.drivers         hive:hive2://,exec:exec://     Comma separated list of [Name]:[Connection URI]
EXPLORER_DRIVER_DIR          zeppelin.driver.dir      drivers			     Explorer driver directory.
=========================    =======================  ============================== ===========


Configure to use Stratio Ingestion Interpreter
----------------------------------------------

If you have installed Stratio Ingestion and you want to use this Interpreter you must edit file  ./conf/ingestion.conf 

.. code-block:: bash

  ingestion.home = /flume-ingestion/stratio-ingestion-0.5.0-SNAPSHOT

You must fill ingestion.home with absolute path where Stratio Ingestion has been installed.

Configure to use Stratio Crossdata Interpreter
----------------------------------------------

If you have installed Stratio Crossdata and you want to use this interperter you must edit file ./conf/driver-application.conf

.. code-block:: bash

  crossdata-driver.config.cluster.name = "CrossdataServerCluster"  
  crossdata-driver.config.cluster.actor = "crossdata-server"  
  crossdata-driver.config.cluster.hosts = ["127.0.0.1:13420"]  
  crossdata-driver.config.retry.times = 3  
  crossdata-driver.config.retry.duration = 120s  

You must also modify ./crossdata/pom.xml

.. code-block:: bash

    <properties> 
        <crossdata.version>HERE_YOUR_CROSSDATA_VERSION</crossdata.version> 
    </properties> 


Configure to use Apacha Cassandra Interpreter
----------------------------------------------

If you have installed Apache Cassabdra and you want to use this interpreter you must edit file ./cassandra/src/main/resources/cassandra.properties

..code-block:: bash

  cassandra.host = 127.0.0.127  
  cassandra.port = 9042    

numebr port and host must be the same numbers where cassadra is raised.  

Start/Stop
^^^^^^^^^^

**Start Explorer**

.. code-block:: bash

  bin/explorer-daemon.sh start

After successful start, visit http://localhost:8084 with your web browser

**Stop Explorer**

.. code-block:: bash

  bin/explorer-daemon.sh stop

