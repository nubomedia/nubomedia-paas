package org.project.openbaton.nubomedia.api;

import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.api.messages.*;
import org.project.openbaton.nubomedia.api.openshift.exceptions.DuplicatedException;
import org.project.openbaton.nubomedia.api.openshift.exceptions.UnauthorizedException;
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
        return new ResponseEntity<Object>(body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleUnauthorized(Exception e, WebRequest request){
        logger.info("handling UnauthorizedException from " + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NubomediaUnauthorizedMessage body = new NubomediaUnauthorizedMessage("Wrong authentication",e.getMessage());
        return new ResponseEntity<Object>(body,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({SDKException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleSDK (Exception e, WebRequest request){
        logger.info("handling SDKException from " + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NubomediaOpenbatonMessage body = new NubomediaOpenbatonMessage("Bad Request",e.getMessage());
        return new ResponseEntity<Object>(body,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler({DuplicatedException.class})
//    @ResponseStatus(HttpStatus.CONFLICT)
//    protected ResponseEntity<Object> handleDup (Exception e, WebRequest request){
//        logger.info("handling duplicate from " + request.getDescription(true));
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        NubomediaDupMessage body = new NubomediaDupMessage(e.getMessage(), request.getParameter("token"));
//        return new ResponseEntity<Object>(body,HttpStatus.CONFLICT);
//    }

}
