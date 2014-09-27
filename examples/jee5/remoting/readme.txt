Seam JEE 5 Remoting (Helloworld) Example
========================================

This example demonstrates the use of Seam in a Java EE 5 environment.
It runs on JBoss AS 5,6, Glassfish V2, and OCFJ 11g as an EAR.

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the remoting-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-jee5-remoting

The following is a list of profiles which you should use to build the 
application for various application servers.

Glassfish V2    ->  -Pglassfish2 (i.e. mvn clean package -Pglassfish2)
OC4J 11g        ->  -Poc4j

Furthermore, when you're targeting a different application server than 
JBossAS 5 or 6, you have to deploy the application manually.
