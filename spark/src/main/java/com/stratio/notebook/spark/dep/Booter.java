package com.stratio.notebook.spark.dep;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;

import com.stratio.notebook.conf.ExplorerConfiguration;

public class Booter {
  public static RepositorySystem newRepositorySystem() {
    return RepositorySystemFactory.newRepositorySystem();
  }

  public static RepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
    MavenRepositorySystemSession session = new MavenRepositorySystemSession();

    ExplorerConfiguration conf = ExplorerConfiguration.create();
    LocalRepository localRepo = new LocalRepository(conf.getRelativeDir(conf.getString("EXPLORER_DEP_LOCAL_REPO",
            "zeppelin.dep.localrepo", "local-repo")));
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo));

    //session.setTransferListener(new ConsoleTransferListener());
    //session.setRepositoryListener(new ConsoleRepositoryListener());

    // uncomment to generate dirty trees
    // session.setDependencyGraphTransformer( null );

    return session;
  }

  public static RemoteRepository newCentralRepository() {
    return new RemoteRepository("central", "default", "http://repo1.maven.org/maven2/");
  }
}