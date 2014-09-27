Seam Guice Example
======================
This is a simple example demonstrating the use of Google Guice with Seam.

example.name=guice

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the guice-ear directory run:

    mvn jboss:hard-deploy

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting WAR from guice-web/target directory to Tomcat manually.
