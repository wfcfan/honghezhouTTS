package com.acctrue.tts.rpc.exception;

public class RpcException extends Exception {

	private static final long serialVersionUID = 0x4eb9514df3acd4f3L;
	private int statusCode;

	public int getStatusCode() {

		return statusCode;
	}

	public void setStatusCode(int i) {

		statusCode = i;
	}

	public RpcException() {

		statusCode = -1;
	}

	public RpcException(int i) {

		statusCode = -1;
		statusCode = i;
	}

	public RpcException(String s) {

		super(s);
		statusCode = -1;
	}

	public RpcException(String s, Throwable throwable) {

		super(s, throwable);
		statusCode = -1;
	}

	public RpcException(Throwable throwable) {

		super(throwable);
		statusCode = -1;
	}
}
