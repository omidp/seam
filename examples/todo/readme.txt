Seam Todo List Example
======================

This is a simple example for the Seam tutorial, demonstrating the
use of jBPM-based business process management.

To deploy the example to JBoss AS, follow these steps:

* In the example root directory run:

    mvn clean package

* Set JBOSS_HOME environment property.

* In the tasks-ear directory run:

    mvn jboss:hard-deploy

* Open this URL in a web browser: http://localhost:8080/seam-todo

To deploy the example to Tomcat with Embedded JBoss, follow these steps:

* In the example root directory run:

    mvn clean package -Ptomcat

* Deploy the resulting war from todo-web/target directory to Tomcat manually

* Open this URL in a web browser: http://localhost:8080/jboss-seam-todo
