Seam Booking Example
====================

This example demonstrates the use of Seam in a Java EE 5 environment.
Transaction and persistence context management is handled by the
EJB container. This example runs on JBoss AS as an EAR or Tomcat 
with JBoss Embedded as a WAR.

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the booking-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-booking

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from booking-web/target directory to Tomcat manually.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-booking

To deploy the example to a cluster, follow these steps:

* First follow the steps 1-9 clustering-howto.txt in the root folder of the Seam distribution.

* In the example root directory run:

    mvn clean package -Pcluster

* Deploy the resulting ear from booking-ear/target directory to $JBOSS_HOME/server/all/farm 
  manually along with a datasource (booking-ear/src/main/resources/jboss-seam-booking-ds.xml)


Running clustering functional tests
===================================

To run functional tests in a cluster, follow these steps:

* Start two JBossAS instances (being supposed to have two JBossAS configurations - all and all2):

- Start first (master) instance of JBossAS: 
    $JBOSS_HOME/bin/run.sh -c all -g DocsPartition -u 239.255.101.101 -b localhost 
    -Djboss.messaging.ServerPeerID=1 -Djboss.service.binding.set=ports-default
- Start second (slave) instance of JBossAS:
    JBOSS_HOME/bin/run.sh -c all2 -g DocsPartition -u 239.255.101.101 -b localhost 
    -Djboss.messaging.ServerPeerID=2 -Djboss.service.binding.set=ports-01

* Set JBOSS_HOME environment property

* In the example root directory run:

    mvn clean verify -Pcluster,ftest-jbossas-cluster

This command will deploy the application along with a datasource to $JBOSS_HOME/server/all/farm 
directory and execute a fail-over test. The main goal of this test is to simulate recovering 
from a breakdown.

