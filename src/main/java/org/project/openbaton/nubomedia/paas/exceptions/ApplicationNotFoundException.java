package org.project.openbaton.nubomedia.paas.exceptions;

/**
 * Created by maa on 21.10.15.
 */
public class ApplicationNotFoundException extends Exception {

    public ApplicationNotFoundException(){super();}

    public ApplicationNotFoundException(Throwable throwable){ super(throwable);}

    public ApplicationNotFoundException(String message){ super(message);}

    public ApplicationNotFoundException(String message, Throwable throwable){ super(message,throwable); }

}
