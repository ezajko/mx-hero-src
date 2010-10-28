package org.mxhero.console.backend.infrastructure;

import org.springframework.flex.core.ExceptionTranslator;
import org.springframework.util.ClassUtils;

import flex.messaging.MessageException;

public class JavaToFlexExceptionTranslator implements ExceptionTranslator {

	@Override
	public boolean handles(Class<?> clazz) {
		if(ClassUtils.isAssignable(clazz, BusinessException.class)){
			return true;
		}
		return false;
	}

	@Override
	public MessageException translate(Throwable throwable) {
		MessageException message = new MessageException();
		if(throwable!=null){
			message.setRootCause(throwable);
			message.setMessage(throwable.getMessage());
			if(throwable instanceof BusinessException){
				message.setCode(((BusinessException)throwable).getCode());
			}
		}
		
		return message;
	}

}
