Seam Wicket Booking Example
===========================

This is a port of the Booking example to Wicket. This example runs on 
JBoss AS as an EAR.

To deploy the example to JBoss AS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the wicket-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-wicket

By default, Java classes are instrumented by Wicket at compile time. You can use
a runtime instrumentation issuing -Pruntime-instrumentation when building the 
application, though:
    
    mvn clean package -Pruntime-instrumentation

You can also execute functional tests using runtime instrumentation:

    mvn clean verify -Pruntime-instrumentation,ftest-jbossas

In order to run functional tests with compile instrumentation, you must use:

    mvn clean verify -Pcompile-instrumentation,ftest-jbossas
