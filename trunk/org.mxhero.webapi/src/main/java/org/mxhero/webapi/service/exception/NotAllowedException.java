package org.mxhero.webapi.service.exception;

public class NotAllowedException extends RuntimeException{

	public NotAllowedException(String msg) {
        super(msg);
    }
}
