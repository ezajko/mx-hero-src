package org.mxhero.engine.commons.connector;

import org.mxhero.engine.commons.mail.MimeMail;

public interface InputServiceFilter {

	void dofilter(MimeMail mail);
}
