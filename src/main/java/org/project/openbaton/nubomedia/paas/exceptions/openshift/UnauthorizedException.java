package org.project.openbaton.nubomedia.paas.exceptions.openshift;

/**
 * Created by maa on 07.10.15.
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException(){super();}

    public UnauthorizedException(Throwable throwable){ super(throwable);}

    public UnauthorizedException(String message){ super(message);}

    public UnauthorizedException(String message, Throwable throwable){ super(message,throwable); }

}
