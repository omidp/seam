package org.jboss.seam.test.integration;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.core.ConversationEntries;
import org.jboss.seam.core.ConversationEntry;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.Switcher;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ConversationTest 
    extends JUnitSeamTest
{
	@Deployment(name="ConversationTest")
	@OverProtocol("Servlet 3.0") 
	public static Archive<?> createDeployment()
	{
		return Deployments.defaultSeamDeployment();
	}
	
    @Test
    public void conversationStack() 
        throws Exception 
    {
        // no conversation, no stack
        new FacesRequest("/pageWithDescription.xhtml") {
            @SuppressWarnings("unchecked")
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
                Assert.assertEquals(0, entries.size());                            
            }
        }.run();
                
       // no conversation, no stack
       new FacesRequest("/pageWithoutDescription.xhtml") {
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginConversation();
           }
           
           @SuppressWarnings("unchecked")
         @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
               Assert.assertEquals(0, entries.size());              
           }
       }.run();
              
       // new conversation, stack = 1
       String rootId = new FacesRequest("/pageWithDescription.xhtml") {
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginConversation();
           }
           
           @SuppressWarnings("unchecked")
         @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
               Assert.assertEquals(1, entries.size());
           }
       }.run();
       
       // nested conversation, stack =2
       String nested1 = new FacesRequest("/pageWithDescription.xhtml", rootId) {
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginNestedConversation();
           }
           
           @SuppressWarnings("unchecked")
         @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
               Assert.assertEquals(2, entries.size());                         
           }
                    
       }.run();
          
       // nested conversation without description, not added to stack
       String nested2 = new FacesRequest("/pageWithoutDescription.xhtml", nested1) {       
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginNestedConversation();
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               Assert.assertEquals(2, entries.size());  
           }
       }.run();
              
       // access a page, now it's on the stack
       new FacesRequest("/pageWithDescription.xhtml", nested2) {
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               Assert.assertEquals(3, entries.size());  
           }
       }.run();
              
       // end conversation, stack goes down
       new FacesRequest("/pageWithDescription.xhtml", nested2) {       
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().endConversation(false);
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               Assert.assertEquals(2, entries.size());  
           }
       }.run();
              
       // end another one, size is 1
       new FacesRequest("/pageWithDescription.xhtml", nested1) {       
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().endConversation(false);
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               Assert.assertEquals(1, entries.size());  
           }
       }.run();
    }
   
    @Test
    public void conversationList() 
        throws Exception 
    {        
        new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(0, entries.size());  
            }
        }.run();
        
        new FacesRequest("/pageWithoutDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(0, entries.size());  
            }
        }.run();
        
        String conv1 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(1, entries.size());  
            }
        }.run();
        
        String conv2 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(2, entries.size());  
            }
        }.run();
        
        String conv3 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(3, entries.size());  
            }
        }.run();
        
        
        new FacesRequest("/pageWithDescription", conv2) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(2, entries.size());  
            }
        }.run();
        
        new FacesRequest("/pageWithDescription", conv1) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(1, entries.size());  
            }
        }.run();
        
        
        new FacesRequest("/pageWithDescription", conv3) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                Assert.assertEquals(0, entries.size());  
            }
        }.run();
    }

    
    @Test
    public void switcher() 
        throws Exception 
    {
        new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(0, switcher.getSelectItems().size());
                Assert.assertNull(switcher.getConversationIdOrOutcome());
            }
        }.run();
        
        
        final String conv1 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(1, switcher.getSelectItems().size());
            }
        }.run();
        
        final String conv2 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(2, switcher.getSelectItems().size());
            }
        }.run();
        
        final String conv3 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(3, switcher.getSelectItems().size());
            }
        }.run();
    
        new FacesRequest() {
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(3, switcher.getSelectItems().size());
                
                List<SelectItem> items = switcher.getSelectItems();
                List<String> values = new ArrayList<String>();
                for (SelectItem item: items) {
                    Assert.assertEquals("page description", item.getLabel());
                    values.add((String) item.getValue());
                }
                
                Assert.assertTrue(values.contains(conv1));
                Assert.assertTrue(values.contains(conv2));
                Assert.assertTrue(values.contains(conv3));
            }
        }.run();
        
        new FacesRequest("/pageWithDescription.xhtml", conv1) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
        }.run();
        
        new FacesRequest("/pageWithDescription.xhtml", conv2) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(1, switcher.getSelectItems().size());
                Assert.assertEquals("page description", switcher.getSelectItems().get(0).getLabel()); 
                Assert.assertEquals(conv3, switcher.getSelectItems().get(0).getValue());
            }
        }.run();
        
        new FacesRequest("/pageWithAnotherDescription.xhtml", conv3) {
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                Assert.assertEquals(1,switcher.getSelectItems().size());
                
                Assert.assertEquals("another page description", switcher.getSelectItems().get(0).getLabel());
                Assert.assertEquals(conv3, switcher.getSelectItems().get(0).getValue());
            }
        }.run();
    }

   @Test
   public void killAllOthers() throws Exception
   {
      new FacesRequest("/pageWithAnotherDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            Assert.assertEquals(1,ConversationEntries.instance().size());
         }
      }.run();

      new FacesRequest("/pageWithoutDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            Assert.assertEquals(2,ConversationEntries.instance().size());
         }
      }.run();

      new FacesRequest("/pageWithDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
            Manager.instance().killAllOtherConversations();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            Assert.assertEquals(1,ConversationEntries.instance().size());
         }
      }.run();

   }

   @Test
   public void nestedKillAllOthers() throws Exception
   {

      final String unrelated = new FacesRequest("/pageWithoutDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            Assert.assertEquals(1,ConversationEntries.instance().size());
         }
      }.run();

      String root = new FacesRequest("/pageWithDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            Assert.assertEquals(2,ConversationEntries.instance().size());
         }
      }.run();

      // nested conversation
      new FacesRequest("/pageWithDescription.xhtml", root) {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginNestedConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            Assert.assertEquals(3,ConversationEntries.instance().size());

            Manager.instance().killAllOtherConversations();
            Assert.assertEquals(2,ConversationEntries.instance().size());
            Assert.assertEquals(true,ConversationEntries.instance().getConversationIds()
                  .contains(unrelated) == false);
         }

      }.run();
   }
}
