package org.project.openbaton.nubomedia.paas.api;

import org.project.openbaton.nubomedia.paas.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.paas.messages.NubomediaAppNotFoundMessage;
import org.project.openbaton.nubomedia.paas.messages.NubomediaDupMessage;
import org.project.openbaton.nubomedia.paas.messages.NubomediaUnauthorizedMessage;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.NameStructureException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by maa on 21.10.15.
 */
@ControllerAdvice
public class NubomediaExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ApplicationNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleAppNotFound(Exception e,WebRequest request){
        logger.info("handling ApplicationNotFoundException from " + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NubomediaAppNotFoundMessage body = new NubomediaAppNotFoundMessage(request.getParameter("id"),request.getHeader("Auth-token"));
        return handleExceptionInternal(e,body,headers,HttpStatus.NOT_FOUND,request);

    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleUnauthorized(Exception e, WebRequest request){
        logger.info("handling UnauthorizedException from " + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NubomediaUnauthorizedMessage body = new NubomediaUnauthorizedMessage("Wrong authentication",e.getMessage());
        return handleExceptionInternal(e,body,headers,HttpStatus.UNAUTHORIZED,request);
    }

    @ExceptionHandler({DuplicatedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleDup (Exception e, WebRequest request){
        logger.info("handling duplicate from " + request.getDescription(true));
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        NubomediaDupMessage body = new NubomediaDupMessage(e.getMessage(), request.getParameter("token"));
        return handleExceptionInternal(e,body,header,HttpStatus.CONFLICT,request);
    }

    @ExceptionHandler({NameStructureException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleNameToLong(Exception e, WebRequest request){
        logger.info("Handling to long name from " + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = e.getMessage();
        return handleExceptionInternal(e,body,headers,HttpStatus.BAD_REQUEST,request);
    }

    @ExceptionHandler({turnServerException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    protected ResponseEntity<Object> handleTurnParametersException(Exception e, WebRequest request){
        logger.info("Handling parameters from" + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "Turn server requires authentication parameters";
        return handleExceptionInternal(e,body,headers,HttpStatus.PRECONDITION_REQUIRED,request);
    }


}
