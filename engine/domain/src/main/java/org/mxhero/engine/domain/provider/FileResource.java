package org.mxhero.engine.domain.provider;

import java.io.InputStream;

public class FileResource implements Resource{

	private InputStream resource;
	
	private String type;
	
	private String name;

	public InputStream getResource() {
		return resource;
	}

	public void setResource(InputStream resource) {
		this.resource = resource;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
