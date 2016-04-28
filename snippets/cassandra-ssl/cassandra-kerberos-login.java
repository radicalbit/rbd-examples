package io.radicalbit.loginexample;

import com.datastax.driver.core.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.radicalbit.cassandra.kerberosauthentication.KerberosAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginExample {

  public static Logger logger = LoggerFactory.getLogger(LoginExample.class);

  public static void main(String[] args) {

    LoginExample l = new LoginExample();
    if (args.length != 1){
      System.out.println("Specify host name/IP");
      System.exit(-1);
    }

    l.execute(args[0]);
  }

  private void execute(String host) {
    Cluster cluster;
    Session session;

    Config config = ConfigFactory.load();

    logger.info("Login into {}", host);

    // Connect to the cluster and keyspace "demo"
    cluster = Cluster.builder()
      .addContactPoint(host)
      .withAuthProvider(new KerberosAuthenticationProvider())
      .build();

    session = cluster.newSession();

    session.execute(" CREATE KEYSPACE IF NOT EXISTS krb WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");


    session.execute("use krb;");
    session.execute("CREATE TABLE IF NOT EXISTS songs (title text, lyrics text, PRIMARY KEY (title))");

    // Insert one record into the users table
    session.execute("INSERT INTO songs (title, lyrics) VALUES ('Don''t believe the hype', 'Caught you lookin for the same thing It''s a new thing check out this I bring')");

    // Use select to get the user we just entered
    ResultSet results = session.execute("SELECT * FROM songs Limit 200;");
    for (Row row : results) {
      logger.info("*** data extracted ***");
      logger.info("{} - {}", row.getString("title"), row.getString("lyrics"));
    }

    session.close();

    // Clean up the connection by closing it
    cluster.close();
  }
}
