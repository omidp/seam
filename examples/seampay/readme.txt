Seam Seampay Example
====================

This example shows Seam's EJB3 timer support. It runs on JBoss AS as an EAR
and Tomcat with Embedded JBoss as a WAR.

To deploy the example to JBoss AS, follow these steps:

* In the example root directory, run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the seampay-ear directory, run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-seampay

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory, run:

    mvn clean package -Ptomcat

* Deploy the resulting war from seampay-web/target directory to Tomcat manually.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-seampay
