package org.jboss.seam.test.integration;

import java.io.File;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class Deployments {
   public static WebArchive defaultSeamDeployment() {
      return defaultSeamDeployment("WEB-INF/components.xml");
   }
   
   // Deployment that use the proper SeamListener instead of the MockSeamListener
   public static WebArchive realSeamDeployment() {
      return ShrinkWrap.create(ZipImporter.class, "test.war").importFrom(new File("target/seam-integration-tests.war")).as(WebArchive.class)
            .addAsWebInfResource(new StringAsset(
                  "<jboss-deployment-structure>" +
                        "<deployment>" +
                        "<dependencies>" +
                        "<module name=\"org.javassist\"/>" +
                        "<module name=\"org.dom4j\"/>" +
                        "<module name=\"org.apache.commons.collections\"/>" +
                        "</dependencies>" +
                        "</deployment>" +
                  "</jboss-deployment-structure>"), "jboss-deployment-structure.xml")
                  .addAsResource("seam.properties")
                  .addAsResource("components.properties")
                  .addAsResource("messages_en.properties")
                  .addAsResource("META-INF/persistence.xml")

                  .addAsResource("hibernate.cfg.xml")
                  .addAsWebInfResource("WEB-INF/components.xml", "components.xml")
                  .addAsWebInfResource("WEB-INF/pages.xml", "pages.xml")
                  .addAsWebInfResource("WEB-INF/real-web.xml", "web.xml")
                  .addAsWebInfResource("WEB-INF/ejb-jar.xml", "ejb-jar.xml")
                  .addAsWebInfResource("WEB-INF/jboss-seam-integration-tests-hornetq-jms.xml", "jboss-seam-integration-tests-hornetq-jms.xml");
   }

   public static WebArchive jbpmSeamDeployment() {
      return ShrinkWrap.create(ZipImporter.class, "test.war").importFrom(new File("target/seam-integration-tests.war")).as(WebArchive.class)
            .addAsWebInfResource(new StringAsset(
                  "<jboss-deployment-structure>" +
                        "<deployment>" +
                        "<dependencies>" +
                        "<module name=\"org.javassist\"/>" +
                        "<module name=\"org.dom4j\"/>" +
                        "<module name=\"org.apache.commons.collections\"/>" +
                        "</dependencies>" +
                        "</deployment>" +
                  "</jboss-deployment-structure>"), "jboss-deployment-structure.xml")
                  .addAsResource("seam.properties")
                  .addAsResource("components.properties")
                  .addAsResource("messages_en.properties")
                  .addAsResource("META-INF/persistence.xml")

                  .addAsResource("testProcess1.jpdl.xml")
                  .addAsResource("testProcess2.jpdl.xml")
                  .addAsResource("testProcess3.jpdl.xml")
                  .addAsResource("testProcess4.jpdl.xml")

                  .addAsResource("jbpm.cfg.xml")

                  .addAsResource("hibernate.cfg.xml")
                  .addAsWebInfResource("WEB-INF/components-jbpm.xml", "components.xml")
                  .addAsWebInfResource("WEB-INF/pages.xml", "pages.xml")
                  .addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                  .addAsWebInfResource("WEB-INF/ejb-jar.xml", "ejb-jar.xml");
   }

   public static WebArchive defaultSeamDeployment(String customComponentsXml) {
      WebArchive war = ShrinkWrap.create(ZipImporter.class, "test.war").importFrom(new File("target/seam-integration-tests.war")).as(WebArchive.class)
            .addAsWebInfResource(new StringAsset(
                  "<jboss-deployment-structure>" +
                        "<deployment>" +
                        "<dependencies>" +
                        "<module name=\"org.javassist\"/>" +
                        "<module name=\"org.dom4j\"/>" +
                        "</dependencies>" +
                        "</deployment>" +
                  "</jboss-deployment-structure>"), "jboss-deployment-structure.xml")
                  .addAsResource("seam.properties")
                  .addAsResource("components.properties")
                  .addAsResource("messages_en.properties")
                  .addAsResource("META-INF/persistence.xml")
   
                  .addAsResource("hibernate.cfg.xml")
                  .addAsWebInfResource(customComponentsXml, "components.xml")
                  .addAsWebInfResource("WEB-INF/pages.xml", "pages.xml")
                  .addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                  .addAsWebInfResource("WEB-INF/ejb-jar.xml", "ejb-jar.xml")
                  .addAsWebInfResource("WEB-INF/jboss-seam-integration-tests-hornetq-jms.xml", "jboss-seam-integration-tests-hornetq-jms.xml");
      
      // Remove jboss-seam-ui for a Mock SeamTest test as it would cause Mojarra to initialize
      for (ArchivePath path : war.getContent().keySet()) {
         if (path.get().contains("jboss-seam-ui")) {
            war.delete(path);
            break;
         }
      }
      
      return war;
   }
}
