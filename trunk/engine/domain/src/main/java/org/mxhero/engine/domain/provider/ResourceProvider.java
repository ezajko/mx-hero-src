package org.mxhero.engine.domain.provider;

import java.io.InputStream;

public interface ResourceProvider {

	InputStream getResource();
	
	String getResourceType();
}
