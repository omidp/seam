Seam UI Example
===============

This is a simple example demonstrating Seam UI. It runs on JBoss
AS as an EAR

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the ui-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-ui
