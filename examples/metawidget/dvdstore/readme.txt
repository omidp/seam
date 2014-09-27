Seam Metawidget DVD Store Example
=================================

This example demonstrates the use of Seam with jBPM pageflow and business
process management. It uses metawidget to dynamically generate the forms at
runtime. It runs on JBoss AS as an EAR.

A majority of source files in this project comes from non-metawidget Dvdstore example
in the distribution. Metawidget source files are placed under src/metawidget 
subdirectories in dvdstore-ejb and dvdstore-web submodules.

To deploy the example to JBossAS 5, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the dvdstore-ear directory run:

    mvn jboss:hard-deploy

JBossAS 4.2 needs additional Hibernate libraries, use a -Pjbossas42 maven profile instead to 
package the application.

JBossAS 6 needs new Hibernate Search with dependencies, and more source code enhancement due to
Hibernate Search and Lucene-Core API changes. Use a -Pjbossas6 maven profile instead to package 
the application.

* Open this URL in a web browser: http://localhost:8080/seam-metawidget-dvdstore

For further Metawidget documentation see http://metawidget.org/documentation.html
