Seam Metawidget Booking Example
===============================

This example demonstrates the use of Seam in a Java EE 5 environment.
Transaction and persistence context management is handled by the
EJB container. This example runs on JBoss AS as an EAR or Tomcat 
with JBoss Embedded as a WAR.

A majority of source files in this project comes from non-metawidget Booking example
in the distribution. Metawidget source files are placed under src/metawidget 
subdirectories in booking-ejb and booking-web submodules.

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the booking-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-metawidget-booking

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from booking-web/target directory to Tomcat manually.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-metawidget-booking

To deploy the example to a cluster, follow these steps:

* First follow the steps 1-9 clustering-howto.txt in the root folder of the Seam distribution.

* In the example root directory run:

    mvn clean package -Pcluster

* Deploy the resulting ear from booking-ear/target directory to $JBOSS_HOME/server/all/farm 
  manually along with a datasource (booking-ear/src/main/resources/jboss-seam-booking-ds.xml)

