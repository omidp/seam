
JBoss Seam - Contextual Component framework for Java EE 5
=========================================================
version 2.3.0.ALPHA, October 2011

This software is distributed under the terms of the FSF Lesser Gnu
Public License (see lgpl.txt). 

Get Up And Running Quick
------------------------
   
1. Install JBoss AS 5.1.0.GA.  

2. Set JBOSS_HOME system enviroment property to your 
   JBoss AS installation directory

3. Start JBoss AS by typing "bin/run.sh" in the JBoss home directory

4. In the "examples/booking" directory, type "mvn clean package" and check 
   for any error messages
   
5. In the booking-ear directory run:

    mvn jboss:hard-deploy
   
6. Point your browser to    

   http://localhost:8080/seam-booking/
      
7. Register an account, search for hotels, book a room...

Learn more
----------

* Read the documentation in the "doc/reference/en-US" directory
* Read the online FAQ http://www.seamframework.org/Seam2

Notes for this release
----------------------

Running the examples with embedded JBoss on Tomcat 6 requires the following 
additional JARs to be updated/added to the Tomcat lib directory after normal 
embedded JBoss embedded install

lib/test/hibernate-all.jar
lib/test/thirdparty-all.jar
lib/slf4j-api.jar
lib/sl4j-log4j12.jar
lib/hsqldb.jar
