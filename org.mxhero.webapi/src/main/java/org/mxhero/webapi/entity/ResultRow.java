package org.mxhero.webapi.entity;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="row")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultRow {

	private Map<String, Object> entries;

	public Map<String, Object> getRow() {
		return entries;
	}

	public void setRow(Map<String, Object> row) {
		this.entries = row;
	}
	
}
