package org.mxhero.webapi.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "queryResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultQuery {

	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="rows")
	private List<ResultRow> rows;

	public List<ResultRow> getRows() {
		return rows;
	}

	public void setRows(List<ResultRow> rows) {
		this.rows = rows;
	}

}
