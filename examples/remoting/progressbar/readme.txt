Seam Remoting/Progressbar Example
=================================

This example shows how to create a progress bar using Seam Remoting. 
It runs on JBoss AS as an EAR and on Tomcat with JBoss Embedded as a WAR.

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the progressbar-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-progressbar

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from progressbar-web/target directory to Tomcat manually

* Open this URL in a web browser: http://localhost:8080/jboss-seam-progressbar
