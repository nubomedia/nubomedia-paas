package org.project.openbaton.nubomedia.api.openbaton.exceptions;

/**
 * Created by maa on 28.01.16.
 */
public class turnServerException extends Exception {

    public turnServerException(){super();}

    public turnServerException(Throwable throwable){ super(throwable);}

    public turnServerException(String message){ super(message);}

    public turnServerException(String message, Throwable throwable){ super(message,throwable); }

}
