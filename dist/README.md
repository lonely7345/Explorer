# Distribution archive of Explorer project #

Explorer is distibuted as a single .deb or .rpm archive with the following structure:

## .deb 
```
 /etc/sds/explorer/ -> explorer config files among others  
 /etc/init.d/explorer -> run script  
 /etc/default/ -> support scripts  
 
 /var/log/sds/explorer -> explorer logs  
 /var/run/sds/ -> explorer PID   
 
 /DEBIAN/copyright -> License file  
 /DEBIAN/postinst -> Permission modification post installation script 
 
```

## .rpm 
```
 /etc/sds/explorer/ -> explorer config files among others  
 /etc/rc.d/explorer -> run script  
 /etc/default/ -> support scripts  
 
 /var/log/sds/explorer -> explorer logs  
 /var/run/sds/ -> explorer PID   
 
```

We use maven-assembly-plugin to build it, see pom.xml for details

**IMPORTANT:** _/lib_ subdirectory contains all transitive dependencyes of the explorer-distribution module,
automatialy resoved by maven, except for explicitly exclude _server_, _web_ and _cli_ explorer sub-modules.
