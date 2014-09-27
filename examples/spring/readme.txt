Seam Spring Example
===================

This example shows Seam/Spring integration. This application runs on JBoss AS as
a WAR file.

To deploy the example to JBossAS 5, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the spring-web directory run:

    mvn jboss:hard-deploy

When deploying to JBossAS 4, use a -Pjbossas42 maven profile to package the 
application. Use -Pjbossas6 for JBossAS 6 accordingly.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-spring
