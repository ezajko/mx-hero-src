package org.mxhero.webapi.service.exception;

public class ConflictResourceException extends RuntimeException{

	public ConflictResourceException(String msg){
		super(msg);
	}
}
