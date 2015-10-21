package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.api.messages.NubomediaAppNotFoundMessage;
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
        logger.debug("handling exception from " + request.getDescription(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        NubomediaAppNotFoundMessage body = new NubomediaAppNotFoundMessage(request.getParameter("id"),request.getHeader("Auth-token"));
        return handleExceptionInternal(e,body,headers,HttpStatus.NOT_FOUND,request);
    }

    

}
