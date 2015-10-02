Explorer dependency graph:
--------------
                     hive, hadoop, ...
                       | | |
                       v v v
  Explorer Server  <- Eengine -> Explorer CLI
         +               |
    Explorer web          v
                        EAN



Notebook artifacts:
------------------
Explorer CLI    - Commandline UI             - executable
Explorer Server - Web UI, server to host it  - executable
Explorer Web    - Web UI, clint-side JS app  - HTML+JavaScript; war
Eengine         - Main library               - java library
EAN             - 



Build process:
-------------
compile                => *.class, minify *.js
build modules          => *.jar, war
test                   => UnitTest reports
package -P package-deb => .deb file
package -P package-rpm => .rpm file
integration-test       => selenium over running zeppelin-server (from package)


verify:
 pre-inegration-test   => start Explorer
 integration-test
 post-inegration-test  => stop Explorer
