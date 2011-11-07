package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;

import org.mxhero.engine.commons.mail.command.Result;

public class ResultWrapper {
	
	private Result result;
	private String messageId;
	private String email;
	
	public void setResult(Result result) {
		this.result = result;
	}
	public Result getResult() {
		return result;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}

}
