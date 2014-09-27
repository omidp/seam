Seam Drools Example
===================

This is a simple example for the Seam tutorial, demonstrating the
use of jBPM with Drools. It runs on JBoss AS as an EAR.

To deploy the example to JBossAS 4 (5 or 6), follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the drools-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-drools

