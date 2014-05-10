package com.QCrystalTile.interfaces;

public class InvalidSpaceGroupIDException extends Exception {

	public InvalidSpaceGroupIDException() {
	}

	public InvalidSpaceGroupIDException(String message) {
		super(message);
	}

	public InvalidSpaceGroupIDException(Throwable cause) {
		super(cause);
	}

	public InvalidSpaceGroupIDException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSpaceGroupIDException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
