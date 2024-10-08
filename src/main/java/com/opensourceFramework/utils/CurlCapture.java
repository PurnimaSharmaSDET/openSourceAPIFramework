package com.opensourceFramework.utils;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Logs each HTTP request as CURL command in "curl" log.
 */
public class CurlCapture implements HttpRequestInterceptor {

	private final boolean logStacktrace;
	private final boolean printMultiliner;
	private Logger log = LoggerFactory.getLogger("curl");
	private static Boolean captureAPIDetails;

	protected CurlCapture(Builder b) {
		this.logStacktrace = b.logStacktrace;
		this.printMultiliner = b.printMultiliner;
	}

	public static Builder defaultBuilder(Boolean captureAPIDetails) {
		CurlCapture.captureAPIDetails = captureAPIDetails;
		return new Builder();
	}

	private static void printStacktrace(StringBuffer sb) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for (StackTraceElement traceElement : trace) {
			sb.append("\tat " + traceElement + System.lineSeparator());
		}
	}

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		try {
			String curl = ConvertHttpRequestToCurl.generateCurl(request, printMultiliner);
			StringBuffer message = new StringBuffer(curl);
			if (logStacktrace) {
				message.append(String.format("%n\tgenerated%n"));
				printStacktrace(message);
			}
			if(CurlCapture.captureAPIDetails) {
				log.debug(message.toString());
			}
		} catch (Exception e) {
			log.warn("Failed to generate CURL command for HTTP request", e);
		}
	}

	public static class Builder {

		private boolean logStacktrace = false;
		private boolean printMultiliner = false;

		/**
		 * Configures {@code CurlLoggingInterceptor} to print a stacktrace where curl command has been generated.
		 */
		public Builder logStacktrace() {
			logStacktrace = true;
			return this;
		}

		/**
		 * Configures {@code CurlLoggingInterceptor} to not print a stacktrace where curl command has been generated.
		 */
		public Builder dontLogStacktrace() {
			logStacktrace = false;
			return this;
		}

		/**
		 * Configures {@code CurlLoggingInterceptor} to print a curl command in multiple lines.
		 */
		public Builder printMultiliner() {
			printMultiliner = true;
			return this;
		}

		/**
		 * Configures {@code CurlLoggingInterceptor} to print a curl command in a single line.
		 */
		public Builder printSingleliner() {
			printMultiliner = false;
			return this;
		}

		public CurlCapture build() {
			return new CurlCapture(this);
		}

	}
}
