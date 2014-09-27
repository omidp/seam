Seam Example Applications
=========================
This directory contains the Seam example applications, which have all been
tested on the latest release of JBoss 5.1. Several examples are also working on
Tomcat with Embedded JBoss(running JDK 1.5). Most of the examples have been tested 
on other application servers (4.2 and 6). Consult the readme.txt file in each of 
the examples to see which additional servers the example supports.

Below is a list of examples with a brief description. The name of the example,
refered to later as ${example.name}, is equivalent to the name of the folder
unless the folder name begins with seam, in which case the prefix "seam" is
omitted (i.e. seamspace -> space).

------------------------------------------------------------------------------
If you are just getting started with Seam, it's highly recommended that you
study the jee5/booking example first. This example is designed to run
unmodified on a Java EE 5-compliant application server. Of course, the real
world is never so ideal. Therefore, you'll find modifications that you need to
make for various application servers in the example's readme.txt file. These
instructions can also be found in the Seam reference documentation.
------------------------------------------------------------------------------


blog/                 The Seam blog example, showing how to write
                      RESTful applications using Seam.

booking/              The Seam Booking demo application for EJB 3.0.

contactlist/          The Seam Contact List demo demonstrating use
                      of the Seam application framework.
                      
drools/               A version of the number guessing example that
                      uses Drools with jBPM.

dvdstore/             The Seam DVD Store demo demonstrating jBPM 
                      support in Seam.

excel/                Demo of excel export support.
                      
groovybooking/        The Seam Booking demo ported to Groovy.

guice/                An example demonstrating the use of Google Guice with Seam.

hibernate/            The Seam Booking demo ported to Hibernate3.

icefaces/             The Seam Booking demo with ICEfaces, instead of 
                      Ajax4JSF.

itext/                A demo of the Seam iText integration for generating PDFs.
                      
jee5/booking          The Seam Booking demo ported to the Java EE 5 platforms.
                      
jee5/remoting         The Seam remoting helloworld demo ported to the Java EE 5 
                      platforms.

jpa/                  An example of the use of JPA (provided by Hibernate), runs
                      on many platforms, including non-EE 5 platforms (including
                      plain Tomcat).
                      
mail/                 The Seam mail example demonstrating use of 
                      facelets-based email templating.

messages/             The Seam message list example demonstrating use 
                      of the @DataModel annotation.

metawidget/           The Seam booking, groovybooking, dvdstore examples implemented
                      using metawidget to define the UI forms.
                      
nestedbooking/        The booking example modified to show the use of nested
                      conversations.

numberguess/          The Seam number guessing example, demonstrating
                      jBPM pageflow.

quartz/               A port of the Seampay example to use the Quartz dispatcher.

registration/         A trivial example for the tutorial.

remoting/chatroom/    The Seam Chat Room example, demostrating Seam remoting.

remoting/gwt/         An example of using GWT with Seam remoting.

remoting/helloworld/  A trivial example using Ajax.
                      
remoting/progressbar/ An example of an Ajax progress bar.

restbay/              An example of using Seam with JAX-RS plain HTTP Web Services.

seambay/              An example of using Seam with Web Services.

seamdiscs/            Demonstrates Seam, Trinidad, Ajax4jsf and Richfaces.

seampay/              The Seam Payments demo demonstrating the use of
                      asynchronous methods.
                
seamspace/            The Seam Spaces demo demonstrating Seam security.

spring/               Demonstrates Spring framework integration.

tasks/				  Demonstrates RESTEasy integration.

todo/                 The Seam todo list example demonstrating
                      jBPM business process management.
                      
ui/                   Demonstrates some Seam JSF controls.

wiki/                 A fully featured wiki system based on Seam which
					  is used by seamframework.org. Please read
					  wiki/README.txt for installation instructions.


Deploying and Testing an Example Application
============================================

These are general instructions for deploying Seam examples. Take a look at the 
readme.txt in the example to see if there are any specific instructions.

How to Build and Deploy an Example on JBoss AS
----------------------------------------------

1. Download and unzip JBoss AS 5.1 GA from:
   
   http://jboss.org/jbossas/downloads

2. Make sure you have an up to date version of Seam: 

   http://seamframework.org/Download

3. Build the example by running the following command from the Seam
   "examples/${example.name}" directory:
   
   mvn clean package
   
   NOTE: Firstly, this command will also run unit tests on that example. To skip the tests add 
   -Dmaven.test.skip=true to the maven call. Secondly, there is an option to deploy an "exploded"
   archive. For this purpose, use -Pexploded maven profile.

4. Deploy the example by setting JBOSS_HOME property and running the 
   following command from the Seam "examples/${example.name}/{example.name}-ear" directory:

   mvn jboss:hard-deploy
    
   To undeploy the example, run:

   mvn jboss:hard-undeploy

5. Start JBoss AS by typing:

      mvn jboss:start

6. Point your web browser to:

   http://localhost:8080/seam-${example.name}

   Recall that ${example.name} is the name of the example folder unless the
   folder begins with seam, in which the prefix "seam" is omitted. The
   context path is set in META-INF/application.xml for EAR deployments.

   However, WAR deployments use a different naming convention for the context
   path. If you deploy a WAR example, point your web browser to:

   http://localhost:8080/jboss-seam-${example.name}

   The WAR examples are groovybooking, jpa, hibernate, and spring

NOTE: The examples use the HSQL database embedded in JBoss AS


How to Build and Deploy the Example on Tomcat
---------------------------------------------

1. Download and install Tomcat 6

   NOTE: Due to a bug, you must install Tomcat to a directory
   path with no spaces. The example does not work in a default
   install of Tomcat.
   
2. Install Embedded JBoss as described in the "Configuration" chapter of the
   Seam reference documentation. 
   
3. Make sure you have an up to date version of Seam: 

   http://seamframework.org/Download

4. Build the example by running the following command from the Seam
   "examples/${example.name}" directory:
   
   mvn clean package -Ptomcat
   
   NOTE: This command will also run unit tests on that example. To skip the tests add 
   -Dmaven.test.skip=true to the maven call.

5. Deploy the example by copying the resulting WAR archive to $CATALINA_HOME/webapps. The WAR
   file can be found in "examples/${example.name}/{example.name}-web/target" directory

6. Start Tomcat

7. Point your web browser to:

   http://localhost:8080/jboss-seam-${example.name}
   
   Note that examples deployed to Tomcat use the context path prefix
   jboss-seam- rather than seam- like with the JBoss AS deployment.

   
Running The TestNG Tests
------------------------

TestNG tests are executed during building of the application using:

   mvn clean package -P<maven_profile>

TODO: Running the TestNG Tests in Eclipse


Running functional tests on an example
=======================================

The following steps describe executing of functional tests in general. If particular example
does not contain certain profile, it is simply ignored during the maven call.

* Start JBoss AS 4, 5, 6 or Tomcat
* Set JBOSS_HOME or CATALINA_HOME environment property, respectively

To run functional tests on JBoss AS 4.2:

*   mvn clean verify -Pjbossas42,ftest-jbossas

To run functional tests on JBoss AS 5.1:

*   mvn clean verify -Pjbossas51,ftest-jbossas
    
To run functional tests on JBoss AS 6:

*   mvn clean verify -Pjbossas6,ftest-jbossas
    
To run functional tests on Tomcat with Embedded JBoss:

*   mvn clean verify -Ptomcat,ftest-tomcat


