Seam Contact List Example
=========================

This example demonstrates the Seam CRUD framework. It runs on JBoss AS as an
EAR and Tomcat with JBoss Embedded as a WAR.

To deploy the example to JBossAS 4, JBossAS 5 or JBossAS 6, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the contactlist-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-contactlist

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from contactlist-web/target directory to Tomcat manually

* Open this URL in a web browser: http://localhost:8080/jboss-seam-contactlist
