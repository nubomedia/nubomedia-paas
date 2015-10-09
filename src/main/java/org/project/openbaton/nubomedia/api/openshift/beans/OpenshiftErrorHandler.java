package org.project.openbaton.nubomedia.api.openshift.beans;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by maa on 07.10.15.
 */
public class OpenshiftErrorHandler implements ResponseErrorHandler{
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is2xxSuccessful())
            return false;

        if (response.getStatusCode().value() == 401)
            return true;

        if (response.getStatusCode().value() == 409)
            return true;

        if (response.getStatusCode().is5xxServerError())
            return true;

        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED){
//            throw new UnauthorizedException();
        }

    }
}
