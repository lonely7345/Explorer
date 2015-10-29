=============
Configuration
=============

To configure Stratio Explorer you need to have some configration files
in etc/sds/conf folder.

To connect with Cassandra you need file with name Cassandra and this parameters :


==================  =================  ====================
Parameter           Default Value      Description
==================  =================  ====================
cassandra.hostPort1  127.0.0.1:8080     Cassandra Host1:port1
cassandra.hostPort2  127.0.0.1:8080     Cassandra Host2:port2
      ....                ...                  ...
cassandra.hostPortn  127.0.0.1:8080     Cassandra Host3:port3
==================  =================  ====================

If you want include more cassandra cluster only need add lines with the same structure.


To connect with Crossdata you need file with name driver-application.conf and this parameters:


=======================================  ==========================  =========================================
Parameter                                Default Value               Description
=======================================  ==========================  =========================================
crossdata-driver.config.cluster.name     "CrossdataServerCluster"    Cluster
crossdata-driver.config.cluster.actor    "crossdata-server"          Server 
crossdata-driver.config.cluster.hosts    ["127.0.0.1:13420"]         Host and port
crossdata-driver.config.retry.times      3 	                         Times to retry query
crossdata-driver.config.retry.duration   120s                        Time to wait result in synchronous mode
=======================================  ==========================  =========================================   

To connect with Ingestion you need file with name ingestion.conf and this parameters :

=================  ===============================
Parameter          Description
=================  ===============================
ingestion.home     folder where ingestions jar is
=================  ===============================


Also exist many files created by developers and can not be removed .

- configuration.xls
- driver-application-default.conf
- explorer-env.sh.template
- notebook-site.xml.template
- explorer-site.xml

Next values of explorer-site.xml can be modified.

=======================  ============================================================ 
Parameter                Description
=======================  ============================================================ 
explorer.server.port     port where explorer server listen , next port will be free
explorer.notebook.dir    folder where notebooks will be saved
=======================  ============================================================













