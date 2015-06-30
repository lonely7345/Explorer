# Distribution archive of Zeppelin project #

Zeppelin is distibuted as a single .deb or .rpm archive with the following structure:

## .deb 
```
 /etc/sds/notebook/ -> notebook config files among others  
 /etc/init.d/notebook -> run script  
 /etc/default/ -> support scripts  
 
 /var/log/sds/notebook -> notebook logs  
 /var/run/sds/ -> notebook PID   
 
 /DEBIAN/copyright -> License file  
 /DEBIAN/postinst -> Permission modification post installation script 
 
```

## .rpm 
```
 /etc/sds/notebook/ -> notebook config files among others  
 /etc/rc.d/notebook -> run script  
 /etc/default/ -> support scripts  
 
 /var/log/sds/notebook -> notebook logs  
 /var/run/sds/ -> notebook PID   
 
```

We use maven-assembly-plugin to build it, see pom.xml for details

**IMPORTANT:** _/lib_ subdirectory contains all transitive dependencyes of the zeppelin-distribution module,
automatialy resoved by maven, except for explicitly exclude _server_, _web_ and _cli_ zeppelin sub-modules.
