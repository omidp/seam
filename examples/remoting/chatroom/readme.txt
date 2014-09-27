Seam Chatroom Example
=====================

This example shows using Seam Remoting to subscribe and publish messages to JMS. 
It runs on JBoss AS as an EAR.

NOTE:
JBoss AS 6 has got new default JMS provider Hornetq, which requires different JMS
configuration. JBoss AS 5 can have also Hornetq instead of default JBoss Messaging.

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the chatroom-ear directory run:

    mvn jboss:hard-deploy               (when intending to use JBoss Messaging)

    mvn jboss:hard-deploy -Phornetq     (when intending to use HornetQ)

* Open this URL in a web browser: http://localhost:8080/seam-chatroom

To run functional tests for the example on JBoss AS 5 or 6, run:

    mvn verify -Pftest-jbossas          (add a hornetq profile when intending to use HornetQ)

To run functional tests for the example on JBoss AS 4, run:

    mvn verify -Pftest-jbossas -Djbossas.version=4
