package com.acctrue.tts.rpc.exception;

public class SdcardException extends Exception {

	private static final long serialVersionUID = 0x435e95dd3690f777L;

	public SdcardException() {

	}

	public SdcardException(String s) {

		super(s);
	}

	public SdcardException(String s, Throwable throwable) {

		super(s, throwable);
	}

	public SdcardException(Throwable throwable) {

		super(throwable);
	}
}
