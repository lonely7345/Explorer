=============
Configuration
=============

To configure Stratio Explorer you need to have some configration files
in etc/sds/conf folder.

To connect with Cassandra you need file with name Cassandra and this parameters :


===============  ===============  ==============
Parameter        Default Value    Description
===============  ===============  ==============

cassandra.host   127.0.0.1         Cassandra Host 
cassandra.port   9042              Cassandra Port
===============  ===============  =============== 


To connect with Crossdata you need file with name driver-application.conf and this parameters:


======================================  ==========================  ========================================
Parameter                               Default Value               Description
======================================  ==========================  ========================================

crossdata-driver.config.cluster.name     "CrossdataServerCluster"    Cluster
crossdata-driver.config.cluster.actor    "crossdata-server"          Server 
crossdata-driver.config.cluster.hosts    ["127.0.0.1:13420"]         Host and port
crossdata-driver.config.retry.times       3 	                     times to retry query
crossdata-driver.config.retry.duration    120s                       time to wait result in synchronous mode
======================================  ==========================  =========================================   

To connect with Ingestion you need file with name ingestion.conf and this parameters :

=================  ======================
Parameter          Description
=================  ======================
 ingestion.home     folder where injestions jar is


Also exist many files created by developers and can not be removed .

- configuration.xls
- driver-application-default.conf
- explorer-env.sh.template
- notebook-site.xml.template
- explorer-site.xml

Next values of explorer-site.xml can be modified.

=======================  =========================================================== 
Parameter                Description
=======================  =========================================================== 
explorer.server.port      port where explorer server listen , next port will be free
explorer.notebook.dir     folder where notebooks will be saved
=======================  ===========================================================













