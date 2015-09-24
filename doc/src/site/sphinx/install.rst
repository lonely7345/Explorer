=================
Install Notebook
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

1.- Clone or download project from https://github.com/Stratio/stratio-notebook  
2.- From Shell move to stratio-netebook folder  
3.- Excute maven package

.. code-block::bash
  
  mvn clean package package 

Configure
---------
Configuration can be done by both environment variable and java properties. If both defined, environment variable is
used.

=========================    =======================  ============================== ===========
notebook-env.sh	             notebook-site.xml         Default value  		     Description
=========================    =======================  ============================== ===========
NOTEBOOK_HOME	  		    		   	   		   	     Notebook Home directory
NOTEBOOK_PORT         	     zeppelin.server.port     8084	   		     Notebook server port
NOTEBOOK_JOB_DIR             zeppelin.job.dir         jobs	   		     Notebook persist/load session in this directory. Can be a path or a URI. location on HDFS supported
NOTEBOOK_ZAN_REPO            zeppelin.zan.repo        https://github.com/NFLabs/zan  Remote ZAN repository URL
NOTEBOOK_ZAN_LOCAL_REPO      zeppelin.zan.localrepo   zan-repo	 		     Notebook library local repository. Local filesystem path
NOTEBOOK_ZAN_SHARED_REPO     zeppelin.zan.sharedrepo				     Notebook library shared repository. Location on HDFS. Usufull when your backend (eg. hiveserver) is not running on the sam machine and want to use zeppelin library with resource file(eg. in hive 'ADD FILE 'path'). So your backend can get resource file from shared repository.
NOTEBOOK_DRIVERS             zeppelin.drivers         hive:hive2://,exec:exec://     Comma separated list of [Name]:[Connection URI]
NOTEBOOK_DRIVER_DIR          zeppelin.driver.dir      drivers			     Notebook driver directory.
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

**Start Notebook**

.. code-block:: bash

  bin/notebook-daemon.sh start

After successful start, visit http://localhost:8084 with your web browser

**Stop Notebook**

.. code-block:: bash

  bin/notebook-daemon.sh stop

