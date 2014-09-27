Seam JPA Example
================

This is the Hotel Booking example implemented in Seam POJO and Hibernate JPA.
This application runs on JBoss AS 4.2, 5 and 6, Tomcat with JBoss Embedded
and Glassfish V2.

To deploy the example to JBossAS 5 or 6, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the jpa-web directory run:

    mvn jboss:hard-deploy

The following is a list of profiles which you should use to build the 
application for various application servers.

JBoss AS 4.2                ->  -Pjbossas42  (i.e. mvn clean package -Pjbossas42)
Glassfish V2                ->  -Pglassfish2
Tomcat with JBoss Embedded  ->  -Ptomcat

Furthermore, when you're targeting a different application server than 
JBossAS 5 or 6, you have to deploy the application manually.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-jpa
