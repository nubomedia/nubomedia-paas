package org.project.openbaton.nubomedia.paas.exceptions.openshift;

/**
 * Created by maa on 29.10.15.
 */
public class DuplicatedException extends Exception{

    public DuplicatedException(){super();}

    public DuplicatedException(Throwable throwable){ super(throwable);}

    public DuplicatedException(String message){ super(message);}

    public DuplicatedException(String message, Throwable throwable){ super(message,throwable); }

}
