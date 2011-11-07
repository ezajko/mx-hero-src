package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application;

import javax.mail.BodyPart;

import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;

public interface AttachmentGenerator {
	
	public BodyPart createNewAttach(Message mail);

}
