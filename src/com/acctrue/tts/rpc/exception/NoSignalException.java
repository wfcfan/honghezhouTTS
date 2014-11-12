package com.acctrue.tts.rpc.exception;

public class NoSignalException extends Exception {

	private static final long serialVersionUID = 0x435e95dd3690f777L;

	public NoSignalException() {

	}

	public NoSignalException(String s) {

		super(s);
	}

	public NoSignalException(String s, Throwable throwable) {

		super(s, throwable);
	}

	public NoSignalException(Throwable throwable) {

		super(throwable);
	}
}
