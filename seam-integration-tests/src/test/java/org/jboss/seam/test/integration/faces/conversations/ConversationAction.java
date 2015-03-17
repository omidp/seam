package org.jboss.seam.test.integration.faces.conversations;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("ConversationAction")
@Scope(ScopeType.CONVERSATION)
public class ConversationAction implements Serializable
{
   private static final long serialVersionUID = 1L;

   @Logger
   private Log log;

   @In
   StatusMessages statusMessages;

   @Out(value = "state", scope = ScopeType.SESSION)
   String state = "";

   @Out(value = "foo", scope = ScopeType.CONVERSATION)
   String foo = "";

   @Out(value = "bar", scope = ScopeType.PAGE)
   String bar = "";

   @Begin
   public void begin()
   {
      state += "begin;";
   }

   @End
   public void end()
   {
      state += "end;";
   }

   public void xyzzy()
   {
      state += "xyzzy;";
   }

   public String viewOutput()
   {

      state += "viewOutput;";

      return "output";
   }

   public String redirectOutput()
   {

      state += "redirectOutput;";

      return "output?faces-redirect=true";
   }

   public void raiseException() throws MyException
   {
      state += "raiseException;";
      throw new MyException();
   }

   public void raiseConversationEndingException() throws MyConversationEndingException
   {
      state += "raiseConversationEndingException;";
      throw new MyConversationEndingException();
   }

   public String getState()
   {
      return state;
   }

   public String pagesRender()
   {
      state += "pagesRender;";

      return "render";
   }

   public String pagesRedirect()
   {
      state += "pagesRedirect;";

      return "redirect";
   }

   public void programmaticRedirect()
   {

      state += "programmaticRedirect;";

      Redirect redirect = Redirect.instance();
      redirect.setViewId("/output.xhtml");
      redirect.execute();
   }

   public void programmaticRedirectNoPropagation()
   {

      state += "programmaticRedirectNoPropagation;";

      Redirect redirect = Redirect.instance();
      redirect.setConversationPropagationEnabled(false);
      redirect.setViewId("/output.xhtml");
      redirect.execute();

      // FacesManager.instance().redirect("/output.xhtml", null, false, false);
   }

   /*
       @BypassInterceptors
       public boolean equals(Object other) {
          return super.equals(other);
       }
   */
}
