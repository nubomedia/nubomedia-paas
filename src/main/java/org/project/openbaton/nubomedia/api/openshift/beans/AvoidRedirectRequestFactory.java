package org.project.openbaton.nubomedia.api.openshift.beans;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by maa on 20.10.15.
 */
public class AvoidRedirectRequestFactory extends SimpleClientHttpRequestFactory {

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        connection.setDoInput(true);

        connection.setInstanceFollowRedirects(false);

        if ("PUT".equals(httpMethod) || "POST".equals(httpMethod) ||
                "PATCH".equals(httpMethod) || "DELETE".equals(httpMethod)) {
            connection.setDoOutput(true);
        } else {
            connection.setDoOutput(false);
        }
        connection.setRequestMethod(httpMethod);
    }

}
