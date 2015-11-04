package org.project.openbaton.nubomedia.api.openshift.exceptions;

/**
 * Created by maa on 04.11.15.
 */
public class NameToLongException extends Exception{ //I can't believe that has to be created

    public NameToLongException(){super();}

    public NameToLongException(Throwable throwable){ super(throwable);}

    public NameToLongException(String message){ super(message);}

    public NameToLongException(String message, Throwable throwable){ super(message,throwable); }
}
