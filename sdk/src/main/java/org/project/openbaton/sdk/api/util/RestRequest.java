package org.project.openbaton.sdk.api.util;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.project.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.Date;

/**
 * OpenBaton api request request abstraction for all requester. Shares common data and methods.
 */
public abstract class RestRequest {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final String baseUrl;

//	protected final String url;

    private Gson mapper;
    private String username;
    private String password;

    /**
	 * Create a request with a given url path
	 *
	 */
	public RestRequest(String username, String password, final String nfvoIp, String nfvoPort, String path, String version) {
		this.baseUrl = "http://" + nfvoIp + ":" + nfvoPort + "/api/v" + version + path;
        this.username = username;
        this.password = password;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        this.mapper = builder.create();
    }

    /**
     * Executes a http post with to a given url, while serializing the object content as json
     * and returning the response
     *
     * @param object
     * 				the object content to be serialized as json
     * @return a string containing the response content
     */
    public Serializable requestPost(final Serializable object) throws SDKException {

        try {
            JsonNode fileJSONNode = getJsonNode(object);

            if (username != null && password != null){
                //TODO add correct headers
            }

            // call the api here
            log.debug("Executing post on: " + this.baseUrl);
            HttpResponse<JsonNode> jsonResponse = Unirest.post(this.baseUrl)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(fileJSONNode)
                    .asJson();
//            check response status
            checkStatus(jsonResponse, HttpURLConnection.HTTP_CREATED);
            // return the response of the request
            String result = jsonResponse.getBody().toString();
            log.debug("received: " + result);
            log.debug("Casting it into: " + object.getClass());
//            return mapper.readValue(jsonResponse.getBody().toString(), object.getClass());

            return mapper.fromJson(result, object.getClass());
        } catch (IOException e) {
            // catch request exceptions here
            e.printStackTrace();
            throw new SDKException("Could not http-post or open the object properly");
        } catch (UnirestException e) {
            // catch request exceptions here
            e.printStackTrace();
            throw new SDKException("Could not http-post or open the object properly");
        }
    }

    private JsonNode getJsonNode(Serializable object) throws IOException {
        return new JsonNode(mapper.toJson(object));
    }

    /**
     * Executes a http delete with to a given id
     *
     * @param id
     * 				the id path used for the api request
     */
    public void requestDelete(final String id) throws SDKException {
        try {
            // call the api here
            log.trace("Executing delete on: " + this.baseUrl + "/" + id);
            HttpResponse<JsonNode> jsonResponse = Unirest.delete(this.baseUrl + "/" + id)
                    .asJson();
//            check response status
            checkStatus(jsonResponse, HttpURLConnection.HTTP_NO_CONTENT);

        } catch (UnirestException | SDKException e) {
            // catch request exceptions here
            throw new SDKException("Could not http-delete or the api response was wrong");
        }
    }

    /**
     * Executes a http get with to a given id
     *
     * @param id
     * 				the id path used for the api request
     * @return a string containing he response content
     */
    public Object requestGet(final String id, Class type) throws SDKException {
        String url = this.baseUrl + "/" + id;
        return requestGetWithStatus(url, null, type);
    }

    /**
     * Executes a http get with to a given id, and possible executed an http (accept) status check of the response if an httpStatus is delivered.
     * If httpStatus is null, no check will be executed.
     *
     * @param url
     * 				the id path used for the api request
     * @param httpStatus
     * 	            the http status to be checked.
     * @param type
     * @return a string containing the response content
     */
    private Object requestGetWithStatus(final String url, final Integer httpStatus, Class type) throws SDKException {
        try {
            // call the api here
            log.debug("Executing get on: " + url);
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .asJson();

            // check response status
            if (httpStatus != null) {
                checkStatus(jsonResponse, httpStatus);
            }
            // return the response of the request
            return mapper.fromJson(jsonResponse.getBody().toString(), type);

        } catch (UnirestException e) {
            // catch request exceptions here
            throw new SDKException("Could not http-get properly");
        }
    }

    /**
     * Executes a http get with to a given url, in contrast to the normal get it uses an http (accept) status check of the response
     *
     * @param url
     * 				the url path used for the api request
     * @return a string containing the response content
     */
    public Object requestGetWithStatusAccepted(final String url, Class type) throws SDKException {
        return requestGetWithStatus(url, new Integer(HttpURLConnection.HTTP_ACCEPTED), type);
    }

    /**
     * Executes a http put with to a given id, while serializing the object content as json
     * and returning the response
     *
     * @param id
     * 				the id path used for the api request
     * @param object
     * 				the object content to be serialized as json
     * @return a string containing the response content
     */
    public Serializable requestPut(final String id, final Serializable object) throws SDKException {
        try {
            JsonNode fileJSONNode = getJsonNode(object);

            // call the api here
            log.debug("Executing put on: " + this.baseUrl + "/" + id);
            HttpResponse<JsonNode> jsonResponse = Unirest.put(this.baseUrl + "/" + id)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(fileJSONNode)
                    .asJson();

//          check response status
            checkStatus(jsonResponse, HttpURLConnection.HTTP_ACCEPTED);

            // return the response of the request
            return mapper.fromJson(jsonResponse.getBody().toString(), object.getClass());

        } catch (IOException | UnirestException | SDKException e) {
            // catch request exceptions here
            throw new SDKException("Could not http-put or the api response was wrong or open the object properly");
        }
    }

    /**
     * Check wether a json repsonse has the right http status. If not, an SDKException is thrown.
     *
     * @param jsonResponse
     * 				the http json response
     * @param httpStatus
     * 				the (desired) http status of the repsonse
     */
    private void checkStatus(HttpResponse<JsonNode> jsonResponse, final int httpStatus) throws SDKException {
        if (jsonResponse.getStatus() != httpStatus) {
            log.debug("Status expected: " + httpStatus + " obtained: " + jsonResponse.getStatus());
            throw new SDKException("Received wrong API HTTPStatus");
        }
    }

}
