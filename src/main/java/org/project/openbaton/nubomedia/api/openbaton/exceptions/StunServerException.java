package org.project.openbaton.nubomedia.api.openbaton.exceptions;

/**
 * Created by maa on 29.01.16.
 */
public class StunServerException extends Exception {

    public StunServerException(){super();}

    public StunServerException(Throwable throwable){ super(throwable);}

    public StunServerException(String message){ super(message);}

    public StunServerException(String message, Throwable throwable){ super(message,throwable); }

}
