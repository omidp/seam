package org.jboss.seam.exception;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

/**
 * @author Omid Pourhadi
 *
 */
public class ParametricExceptionHandler extends RuntimeException
{

    private Map<String, Object> parameters;

    public ParametricExceptionHandler()
    {
    }

    public ParametricExceptionHandler(String message)
    {
        super(message);
    }

    public ParametricExceptionHandler(Map<String, Object> parameters)
    {
        this.parameters = parameters;
    }

    public ParametricExceptionHandler(String message, Map<String, Object> parameters)
    {
        super(message);
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters()
    {
        return parameters;
    }

    protected Severity getMessageSeverity()
    {
        return FacesMessage.SEVERITY_INFO;
    }

}
