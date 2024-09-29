package com.opensourceFramework.utils;


import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Builds `RestAssuredConfig` that allows REST-assured to logs each HTTP request as CURL command.
 */
public class CurlBuilder {

    private final CurlCapture.Builder interceptorBuilder;
    private final RestAssuredConfig config;

    public CurlBuilder(RestAssuredConfig config, Boolean captureAPIDetails) {
        this.config = config;
        this.interceptorBuilder = CurlCapture.defaultBuilder(captureAPIDetails);
    }

    public CurlBuilder(Boolean captureAPIDetails) {
        this(RestAssured.config(), captureAPIDetails);
    }


    /**
     * Configures {@link RestAssuredConfig} to print a stacktrace where curl command has been
     * generated.
     */
    public CurlBuilder logStacktrace() {
        interceptorBuilder.logStacktrace();
        return this;
    }

    /**
     * Configures {@link RestAssuredConfig} to not print a stacktrace where curl command has
     * been generated.
     */
    public CurlBuilder dontLogStacktrace() {
        interceptorBuilder.dontLogStacktrace();
        return this;
    }

    /**
     * Configures {@link RestAssuredConfig} to print a curl command in multiple lines.
     */
    public CurlBuilder printMultiliner() {
        interceptorBuilder.printMultiliner();
        return this;
    }

    /**
     * Configures {@link RestAssuredConfig} to print a curl command in a single line.
     */
    public CurlBuilder printSingleliner() {
        interceptorBuilder.printSingleliner();
        return this;
    }

    public RestAssuredConfig build() {
        return config
                .httpClient(HttpClientConfig.httpClientConfig()
                        .reuseHttpClientInstance()
                        .httpClientFactory(new MyHttpClientFactory(interceptorBuilder.build())));

    }

    private static class MyHttpClientFactory implements HttpClientConfig.HttpClientFactory {

        private final CurlCapture curlCapture;

        public MyHttpClientFactory(CurlCapture curlCapture) {
            this.curlCapture = curlCapture;
        }

        @Override
        public HttpClient createHttpClient() {
            AbstractHttpClient client = new DefaultHttpClient();
            client.addRequestInterceptor(curlCapture);
            return client;
        }
    }

}
