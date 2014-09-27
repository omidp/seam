Seam GroovyBooking Example
==========================

This is the Hotel Booking example implemented in Groovy Beans and Hibernate JPA.
This application runs on JBoss AS, but is deployed as an *exploded* WAR rather 
than an EAR.

To deploy the example to JBossAS 5, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the groovybooking-web directory run:

    mvn jboss:hard-deploy

When deploying to JBossAS 4, use a -Pjbossas42 maven profile to package the 
application.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-groovybooking
