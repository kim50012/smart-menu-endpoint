package com.basoft.eorder.application.wx.http;

/**
 * HttpClientException
 * 
 * @author basoft
 */
public class HttpClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HttpClientException() {
	}

	public HttpClientException(String message) {
		super(message);
	}

	public HttpClientException(Throwable cause) {
		super(cause);
	}

	public HttpClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}