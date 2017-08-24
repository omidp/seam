package org.jboss.seam.bpm;

import static org.jboss.seam.annotations.Install.BUILT_IN;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Element;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.AbstractMutable;
import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.node.SubProcessResolver;
import org.jbpm.jpdl.JpdlException;

/**
 * 
 * 
 * @author Omid Pourhadi
 */
@Name("org.jboss.seam.bpm.seamsubprocessresolver")
@Scope(ScopeType.APPLICATION)
@BypassInterceptors
@Startup
@Install(dependencies = "org.jboss.seam.bpm.jbpm", precedence = BUILT_IN)
public class SeamExpressionSubProcessResolver implements SubProcessResolver
{

    @Override
    public ProcessDefinition findSubProcess(Element subProcessElement)
    {
        ProcessDefinition subProcessDefinition = null;

        String subProcessName = subProcessElement.attributeValue("name");
        String subProcessVersion = subProcessElement.attributeValue("version");

        // if this parsing is done in the context of a process deployment, there
        // is
        // a database connection to look up the subprocess.
        // when there is no jbpmSession, the definition will be left null... the
        // testcase can set it as appropriate.
        // JbpmContext jbpmContext = JbpmContext.getCurrentJbpmContext();
        JbpmContext jbpmContext = ManagedJbpmContext.instance();
        if (jbpmContext != null)
        {
            GraphSession graphSession = jbpmContext.getGraphSession();
            if(graphSession == null)
                throw new JpdlException("graphsession can  not be null");
            // now, we must be able to find the sub-process
            if (subProcessName != null)
            {

                // if the name and the version are specified
                if (subProcessVersion != null)
                {

                    try
                    {
                        int version = Integer.parseInt(subProcessVersion);
                        // select that exact process definition as the
                        // subprocess definition
                        subProcessDefinition = graphSession.findProcessDefinition(subProcessName, version);

                    }
                    catch (NumberFormatException e)
                    {
                        throw new JpdlException("version in process-state was not a number: " + subProcessElement.asXML());
                    }

                }
                else
                { // if only the name is specified
                    // select the latest version of that process as the
                    // subprocess
                    // definition
                    subProcessDefinition = graphSession.findLatestProcessDefinition(subProcessName);
                }
            }
            else
            {
                throw new JpdlException("no sub-process name specfied in process-state: " + subProcessElement.asXML());
            }
        }

        return subProcessDefinition;
    }
    
    public static SeamExpressionSubProcessResolver instance()
    {
       if ( !Contexts.isApplicationContextActive() )
       {
          throw new IllegalStateException("no active application context");
       }
       return (SeamExpressionSubProcessResolver) Component.getInstance(SeamExpressionSubProcessResolver.class, ScopeType.APPLICATION);
    }
    
}
