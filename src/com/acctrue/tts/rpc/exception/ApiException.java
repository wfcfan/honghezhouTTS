package com.acctrue.tts.rpc.exception;

public class ApiException extends Exception {

	private static final long serialVersionUID = 0x4353b0a9c84aedd7L;

	private int errorCode;

	private String errorDesc;

	public void setServerError(int code, String desc) {
		this.setErrorCode(code);
		this.setErrorDesc(desc);
	}

	public ApiException() {

	}

	public ApiException(String s) {

		super(s);
	}

	public ApiException(String s, Throwable throwable) {

		super(s, throwable);
	}

	public ApiException(Throwable throwable) {

		super(throwable);
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
