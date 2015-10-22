=============
Configuration
=============

To configure Stratio Explorer you need to have some configration files
in etc/sds/conf folder.

To connect with Cassandra you need file with name Cassandra and this parameters :

-  cassandra.host=127.0.0.1 
-  cassandra.port=9042 


To connect with Crossdata you need file with name driver-application.conf and this parameters:

-	crossdata-driver.config.cluster.name = "CrossdataServerCluster" 
-	crossdata-driver.config.cluster.actor = "crossdata-server" 
-	crossdata-driver.config.cluster.hosts = ["127.0.0.1:13420"] 
-	crossdata-driver.config.retry.times = 3 	// number of retry quey
-	crossdata-driver.config.retry.duration = 120s  //time to wait result in synchronous mode


To connect with Ingestion you need file with name ingestion.conf and this parameters :

	ingestion.home=/instalation_ingestion_folder/flume-ingestion/stratio-ingestion-0.5.0-SNAPSHOT


Also exist many files created by developers and can be remove or modify and should be in 
the same folder etc/sds/conf. This files are :

- configuration.xls
- driver-application-default.conf
- explorer-env.sh.template
- notebook-site.xml.template
- explorer-site.xml

If file explorer-site is removed or modify then Stratio Explorer not run.	











