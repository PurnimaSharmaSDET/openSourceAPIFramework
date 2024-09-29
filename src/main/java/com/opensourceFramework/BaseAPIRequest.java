package com.opensourceFramework;

import com.opensourceFramework.constants.ContentType;
import com.opensourceFramework.constants.MethodType;
import com.opensourceFramework.utils.CurlBuilder;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseAPIRequest {

    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    private MethodType method;
    private Object body;
    private ContentType contentType;
    private String baseUri;
    private Map<String, Object> pathParams = new HashMap<>();
    private Map<String, Object> queryParams = new HashMap<>();
    private Map<String, Object> formURLEncoded = new HashMap<>();
    private Map<String, Object> params = new HashMap<>();
    private String basePath;
    private String cookie;
    private Map<String, Object> headers = new HashMap<>();
    private Response response;
    private Boolean captureAPIDetails = true;
    private Boolean redirectFlag = true;
    private String jsonResponseSchema = "";

    /**
     * @return the JSON response schema string.
     * This can be used to set a schema against which the JSON response is validated.
     */
    public String getJsonResponseSchema() {
        return jsonResponseSchema;
    }

    public void setJsonResponseSchema(String jsonResponseSchema) {
        this.jsonResponseSchema = jsonResponseSchema;
    }

    /**
     * @return whether redirects are followed during the API request.
     */
    public Boolean getRedirectFlag() {
        return redirectFlag;
    }

    public void setRedirectFlag(Boolean redirectFlag) {
        this.redirectFlag = redirectFlag;
    }

    /**
     * @return whether API request and response details are captured.
     * This flag is used to toggle capturing of cURL details.
     */
    public Boolean getCaptureAPIDetails() {
        return captureAPIDetails;
    }

    public void setCaptureAPIDetails(Boolean captureAPIDetails) {
        this.captureAPIDetails = captureAPIDetails;
    }

    /**
     * Sets the HTTP method (GET, POST, PUT, etc.) for the API request.
     * @param method - the HTTP method.
     */
    public void setMethod(MethodType method) {
        this.method = method;
    }

    /**
     * Sets the request body for the API call.
     * @param obj - the request body object.
     */
    public void setBody(Object obj) {
        this.body = obj;
        requestSpecBuilder.setBody(obj);
    }

    /**
     * Sets a byte array as the request body.
     * @param obj - byte array to be used as the request body.
     */
    public void setBody(byte[] obj) {
        this.body = obj;
        requestSpecBuilder.setBody(obj);
    }

    /**
     * Sets basic authentication for the API request.
     * @param userName - the username for authentication.
     * @param password - the password for authentication.
     */
    public void setBasicAuth(String userName, String password) {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(userName);
        authScheme.setPassword(password);
        requestSpecBuilder.setAuth(authScheme);
    }

    /**
     * Sets the content type of the API request.
     * @param contentType - the content type (e.g., JSON, XML).
     */
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
        requestSpecBuilder.setContentType(contentType.getContentType());
    }

    /**
     * Sets the base URI for the API request.
     * @param baseUri - the base URI for the API.
     */
    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        requestSpecBuilder.setBaseUri(baseUri);
    }

    /**
     * Sets a cookie for the API request.
     * @param cookie - the cookie string.
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
        requestSpecBuilder.addCookie(cookie);
    }

    /**
     * Sets the base path for the API request.
     * @param basePath - the base path to be appended to the base URI.
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
        requestSpecBuilder.setBasePath(basePath);
    }

    /**
     * Adds headers to the API request.
     * @param headers - a map of header key-value pairs.
     */
    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        requestSpecBuilder.addHeaders(headers);
    }

    /**
     * Adds a single header to the API request.
     * @param headerKey - the header key.
     * @param headerValue - the header value.
     */
    public void addHeader(String headerKey, String headerValue) {
        this.headers.put(headerKey, headerValue);
        requestSpecBuilder.addHeader(headerKey, headerValue);
    }

    /**
     * Adds a query parameter to the API request.
     * @param paramKey - the query parameter key.
     * @param paramValue - the query parameter value.
     */
    public void addQueryParam(String paramKey, Object paramValue) {
        this.queryParams.put(paramKey, paramValue);
        requestSpecBuilder.addQueryParam(paramKey, paramValue);
    }

    /**
     * Adds multiple query parameters to the API request.
     * @param queryParams - a map of query parameter key-value pairs.
     */
    public void addQueryParams(Map<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        requestSpecBuilder.addQueryParams(queryParams);
    }

    /**
     * Adds a path parameter to the API request.
     * @param paramKey - the path parameter key.
     * @param paramValue - the path parameter value.
     */
    public void addPathParam(String paramKey, Object paramValue) {
        this.pathParams.put(paramKey, paramValue);
        requestSpecBuilder.addPathParam(paramKey, paramValue);
    }

    /**
     * Adds multiple path parameters to the API request.
     * @param pathParams - a map of path parameter key-value pairs.
     */
    public void addPathParams(Map<String, String> pathParams) {
        this.pathParams.putAll(pathParams);
        requestSpecBuilder.addPathParams(pathParams);
    }

    /**
     * Adds form URL-encoded data to the API request.
     * @param paramKey - the form parameter key.
     * @param paramValue - the form parameter value.
     */
    public void addFormURLEncoded(String paramKey, Object paramValue) {
        this.formURLEncoded.put(paramKey, paramValue);
        requestSpecBuilder.addFormParam(paramKey, paramValue);
    }

    /**
     * Adds multiple form URL-encoded parameters to the API request.
     * @param formURLEncoded - a map of form URL-encoded key-value pairs.
     */
    public void addFormURLEncoded(Map<String, Object> formURLEncoded) {
        this.formURLEncoded.putAll(formURLEncoded);
        requestSpecBuilder.addFormParams(formURLEncoded);
    }

    /**
     * Adds a multipart file to the API request.
     * @param controlName - the control name for the multipart data.
     * @param file - the file to upload.
     */
    public void addMultiPart(String controlName, File file) {
        requestSpecBuilder.addMultiPart(controlName, file);
    }

    /**
     * Makes an API call based on the configured HTTP method and request details.
     * @return Response object representing the API response.
     */
    public Response makeAPICall() {
        RequestSpecification requestSpecification = requestSpecBuilder
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        Response response;
        RestAssured.defaultParser = Parser.JSON;
        RestAssuredConfig config = new CurlBuilder(captureAPIDetails).build();

        switch (method) {
            case GET:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).get();
                break;
            case POST:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).post();
                break;
            case PUT:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).put();
                break;
            case DELETE:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).delete();
                break;
            case PATCH:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).patch();
                break;
            case HEAD:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).head();
                break;
            case OPTIONS:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).options();
                break;
            case CONNECT:
                response = given().config(config).spec(requestSpecification).when().redirects().follow(redirectFlag).request("CONNECT");
                break;
            case POSTBYTES:
                response = given().spec(requestSpecification).when().redirects().follow(redirectFlag).post();
                break;
            default:
                throw new UnsupportedOperationException("Method type not supported.");
        }

        return response;
    }
}
