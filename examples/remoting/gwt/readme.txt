Seam Remoting/Gwt Example
=========================

This example shows GWT with Seam remoting. 
It runs on JBoss AS as an EAR.

To deploy the example to JBossAS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the gwt-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-gwt

The Gwt front-end is being generated each time upon issuing "mvn package".
The artifacts are generated in gwt-ejb submodule and later coppied to 
gwt-web submodule's output directory.

This feature was added in Seam 2.3 version. Before that, the Gwt artifacts 
were pre-build in a source directory.

If you want to use the GWT hosted mode, well, read all about it from the 
GWT docs ! A gwt-maven-plugin generating Gwt front-end has this capability, 
though.
