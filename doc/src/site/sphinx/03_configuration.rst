=============
Configuration
=============

To configure Stratio Explorer you need to have some configration files
in etc/sds/conf folder.

To connect with Cassandra you need file with name Cassandra and this parameters :

  cassandra.host=127.0.0.1 

  cassandra.port=9042 

cassandra.host must be filled with host where cassandra listen.

cassandra.port must be filled with port where cassandra listen.


To connect with Crossdata you need file with name driver-application.conf and this parameters:

	crossdata-driver.config.cluster.name = "CrossdataServerCluster" 

	crossdata-driver.config.cluster.actor = "crossdata-server" 

	crossdata-driver.config.cluster.hosts = ["127.0.0.1:13420"] 

	crossdata-driver.config.retry.times = 3 	

	crossdata-driver.config.retry.duration = 120s 


crossdata-driver.config.cluster.name must be filled with cluster name.

crossdata-driver.config.cluster.actor must be filled with crossdata server.

crossdata-driver.config.cluster.hosts must be filled with host where crossdata listen.

crossdata-driver.config.retry.times must be filled with times that query will be retry if not return data.

crossdata-driver.config.retry.duration must be filled with time to wait response in synchronous mode.


To connect with Ingestion you need file with name ingestion.conf and this parameters :

	ingestion.home=/folder/flume-ingestion/stratio-ingestion-0.5.0-SNAPSHOT

ingestion.home must be filled with installation folder of ingestion.

Also exist many files created by developers and can be remove or modify and should be in 
the same folder etc/sds/conf. This files are :

- configuration.xls
- driver-application-default.conf
- explorer-env.sh.template
- notebook-site.xml.template
- explorer-site.xml

If file explorer-site is removed or modify then Stratio Explorer not run.	











