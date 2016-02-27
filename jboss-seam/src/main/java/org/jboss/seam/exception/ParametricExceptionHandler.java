package org.jboss.seam.exception;

import java.util.Map;

/**
 * @author Omid Pourhadi
 *
 */
public class ParametricExceptionHandler extends RuntimeException
{

    private Map<String, Object> parameters;

    public ParametricExceptionHandler(Map<String, Object> parameters)
    {
        super();
        this.parameters = parameters;
    }

    public ParametricExceptionHandler(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
            Map<String, Object> parameters)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.parameters = parameters;
    }

    public ParametricExceptionHandler(String message, Throwable cause, Map<String, Object> parameters)
    {
        super(message, cause);
        this.parameters = parameters;
    }

    public ParametricExceptionHandler(String message, Map<String, Object> parameters)
    {
        super(message);
        this.parameters = parameters;
    }

    public ParametricExceptionHandler(Throwable cause, Map<String, Object> parameters)
    {
        super(cause);
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters()
    {
        return parameters;
    }

}
