Seam IceFaces Example
=====================

This example demonstrates integration with IceFaces. It runs on JBoss AS as an
EAR.

To deploy the example to JBoss AS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the icefaces-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-icefaces

To run functional tests for the example on JBoss AS 5 or 6, run:

    mvn verify -Pftest-jbossas

To run functional tests for the example on JBoss AS 4, run:

    mvn verify -Pftest-jbossas -Djbossas.version=4
