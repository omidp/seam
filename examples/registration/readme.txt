Seam Registration Example
=========================

This is a trivial example for the Seam tutorial. It runs on both JBoss AS as an
EAR and Tomcat with Embedded JBoss as a WAR.

To deploy the example to JBossAS 4, JBossAS 5 or JBossAS 6, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the registration-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-registration

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from registration-web/target directory to Tomcat manually.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-registration
