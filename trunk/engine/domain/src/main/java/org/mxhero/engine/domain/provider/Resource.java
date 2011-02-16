package org.mxhero.engine.domain.provider;

import java.io.InputStream;

public interface Resource {

	
	InputStream getResource();
	
	String getName();
	
	String getType();
	
}
