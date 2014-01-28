package com.bravo.https.util;

public class BravoAuthenticationException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 919483853624258753L;

	public BravoAuthenticationException() {
	}
	
	public BravoAuthenticationException(String msg) {
		super(msg);
	}
	
	public BravoAuthenticationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public BravoAuthenticationException(Throwable cause) {
		super(cause);
	}
}
