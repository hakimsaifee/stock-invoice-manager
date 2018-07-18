package com.sim.common.exception;

public class DBException extends RuntimeException {
	private static final long serialVersionUID = -2227913815137791892L;

	public DBException() {
		super();
	}

	public DBException(final String message) {
		super(message);
	}

	public DBException(final Throwable cause) {
		super(cause);
	}

	public DBException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
