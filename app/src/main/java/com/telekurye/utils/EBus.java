package com.telekurye.utils;

/**
 * Created by sefagurel on 24.3.2015.
 */
public class EBus {

	private int		statusCode;
	private String	message;

	public EBus(int StatusCode, String Message) {
		statusCode = StatusCode;
		message = Message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}