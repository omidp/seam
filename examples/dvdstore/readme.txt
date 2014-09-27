Seam DVD Store Example
======================

This example demonstrates the use of Seam with jBPM pageflow and business
process management. It runs on JBoss AS as an EAR and Tomcat with Embedded
JBoss as a WAR.

To deploy the example to JBossAS 5, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the dvdstore-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-dvdstore

JBossAS 4.2 needs additional Hibernate libraries, use a -Pjbossas42 maven profile instead to 
package the application.

JBossAS 6 needs new Hibernate Search with dependencies, and more source code enhancement due to
Hibernate Search and Lucene-Core API changes. Use a -Pjbossas6 maven profile instead to package 
the application.

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from dvdstore-web/target directory to Tomcat manually.

* Open this URL in a web browser: http://localhost:8080/jboss-seam-dvdstore

