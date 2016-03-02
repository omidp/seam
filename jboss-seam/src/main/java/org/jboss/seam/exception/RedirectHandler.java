package org.jboss.seam.exception;

import java.util.Map;

import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.RedirectException;
import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;
import org.jboss.seam.navigation.Pages;

/**
 * Base implementation of redirection exception handlers.
 * 
 * @author Gavin King
 *
 */
public abstract class RedirectHandler extends ExceptionHandler
{

    private static final LogProvider log = Logging.getLogProvider(RedirectHandler.class);

    protected abstract String getViewId(Exception e);

    protected abstract String getMessage(Exception e);

    protected abstract boolean isEnd(Exception e);

    protected abstract boolean includePageParameters(Exception e);

    protected abstract Severity getMessageSeverity(Exception e);

    @Override
    public void handle(Exception e) throws Exception
    {
        String viewId = getViewId(e);
        if (viewId == null || viewId.trim().length() == 0)
        {
            // we want to perform a redirect straight back to the current page
            // there is no ViewRoot available, so lets do it the hard way
            String servletPath = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                    .getServletPath();
            viewId = servletPath.substring(0, servletPath.lastIndexOf('.')) + Pages.getSuffix();
        }

        if (e instanceof ParametricExceptionHandler)
            addFacesMessage("#0", ((ParametricExceptionHandler) e).getMessageSeverity(), null, getDisplayMessage(e, getMessage(e)));
        else
            addFacesMessage("#0", getMessageSeverity(e), null, getDisplayMessage(e, getMessage(e)));

        if (Contexts.isConversationContextActive() && isEnd(e))
        {
            Conversation.instance().end();
        }

        try
        {
            if (includePageParameters(e))
            {
                Map<String, Object> parameters = Pages.instance().getStringValuesFromPageContext(FacesContext.getCurrentInstance());
                if (e instanceof ParametricExceptionHandler)
                    parameters = ((ParametricExceptionHandler) e).getParameters();
                if(parameters != null)
                    parameters.remove("actionMethod");
                redirect(viewId, parameters);
            }
            else
            {
                redirect(viewId, null);
            }
        }
        catch (RedirectException re)
        {
            // do nothing
            log.debug("could not redirect", re);
        }
    }

    @Override
    public String toString()
    {
        return "RedirectHandler";
    }
}