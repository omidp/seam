Seam JEE5 Examples
==================

The examples in this directory showcases how to build Java EE 5 compliant Seam 
applications. The application should run on all Java EE 5 compliant 
application servers with minimal changes in code and configuration files. The 
default build script build a deployable EAR for JBoss AS 5. See Seam reference
guide for instructions for other containers and readme files for individual 
examples.

GlassFish V2 
------------

1.  Build the demo app by running "mvn clean package -Pglassfish2"

2.  Download GlassFish V2 Final Build

3.  Install it: java -Xmx256m -jar glassfish-installer-xxx.jar

4.  Setup glassfish: cd glassfish; ant -f setup.xml;

5.  Start the GlassFish server: $GLASSFISH_HOME/bin/asadmin start-domain domain1

6.  Start the embedded JavaDB: $GLASSFISH_HOME/bin/asadmin start-database

7.  Load the admin console: http://localhost:4848/

8.  Login using username/password: admin / adminadmin

9.  Deploy the "Enterprise Application" in the admin console
    or using the command $GLASSFISH_HOME/bin/asadmin deploy <application ear file>

10. When you've finished, stop the server and database: 
    $GLASSFISH_HOME/bin/asadmin stop-domain domain1; $GLASSFISH_HOME/bin/asadmin stop-database


OC4J 11g Technology Preview
---------------------------

1.  Build the demo app by running "mvn clean package -Poc4j"

2.  Download OC4J 11g Technology Preview from here 
    http://www.oracle.com/technology/tech/java/oc4j/11/index.html

3.  Unzip the downloaded file

4.  Make sure you have $JAVA_HOME and $ORACLE_HOME set as environment 
    variables ($ORACLE_HOME is the directory to which you unzip OC4J). For 
    further information on installing OC4J, consult the Readme.txt distributed 
    with OC4J

5.  Edit the OC4J datasource $ORACLE_HOME/j2ee/home/config/data-sources.xml 
    and, inside <data-sources>, add

    <managed-data-source 
      connection-pool-name="jee5-connection-pool" 
      jndi-name="jdbc/__default" 
      name="jee5-managed-data-source"
      />
    <connection-pool name="jee5-connection-pool">
      <connection-factory 
        factory-class="org.hsqldb.jdbcDriver" 
        user="sa" 
        password="" url="jdbc:hsqldb:." 
        />
    </connection-pool>


6.  Edit $ORACLE_HOME/j2ee/home/config/server.xml and, inside 
    <application-server>, add

    <application 
      name="seam-jee5-booking" 
      path="../../home/applications/seam-jee5-booking.ear" 
      parent="default" 
      start="true" 
    />

    or

    <application 
      name="seam-jee5-remoting" 
      path="../../home/applications/seam-jee5-remoting.ear" 
      parent="default" 
      start="true" 
    />
    
    (depending on which of the two applications you're gonna use)

7.  Edit $ORACLE_HOME/j2ee/home/config/default-web-site.xml, and, inside 
    <web-site>, add

    <web-app 
      application="seam-jee5-booking" 
      name="seam-jee5-booking" 
      load-on-startup="true" 
      root="/seam-jee5-booking" 
    />

    or
    
    <web-app 
      application="seam-jee5-remoting" 
      name="seam-jee5-remoting" 
      load-on-startup="true" 
      root="/seam-jee5-remoting" 
    />
    
    (depending on which of the two applications you're gonna use)

8.  Copy hsqldb.jar to OC4J: 
    cp ../../lib/hsqldb.jar $ORACLE_HOME/j2ee/home/applib/

9. Copy the application to OC4J: 
    cp build/jboss-seam-jee5.ear $ORACLE_HOME/j2ee/home/applications/

10. Start OC4J: $ORACLE_HOME/j2ee/home/java -jar -XX:MaxPermSize=256M oc4j.jar
    * You must override the default PermGen memory settings using above command
       * See http://www.oracle.com/technology/tech/java/oc4j/11/oc4j-relnotes.html
    * You will be asked to set the admin password if this is the first time 
      you've started OC4J
    * You may get an ClassNotFoundException relating to 
      org.jboss.logging.util.OnlyOnceErrorHandler, this doesn't impact on the 
      running of the app.  We are working to get rid of this error!

11. Checkout the app at: http://localhost:8888/seam-jee5-booking 
    (http://localhost:8888/seam-jee5-remoting)

12. You can stop the server by pressing CTRL-c in the console on which the 
    server is running.


Workarounds for OC4J 11g
------------------------

* Set hibernate.query.factory_class=org.hibernate.hql.classic.ClassicQueryTranslatorFactory in
  persistence.xml - OC4J uses an incompatible (old) version of antlr in toplink which causes 
  hibernate to throw an exception (discussed here for Weblogic, but the same applies to OC4J - 
  http://hibernate.org/250.html#A23).  You can also work around this by putting the hibernate 
  jars in $ORACLE_HOME/j2ee/home/applib/
  
  (this is done automatically when using -Poc4j profile, only needed for a booking application)
  
  
WebLogic 10.3
---------------------------

Weblogic support requires some specific patches from Oracle/BEA so that their
EJB3 support fuctions correctly.  Please refer to the Seam reference guide for 
additional information.
  
- http://seamframework.org/Documentation
  
  
WebSphere 7
-----------

The instructions for integration with WebSphere are fairly verbose. Please 
refer to the Seam reference guide for additional information.

- http://seamframework.org/Documentation
