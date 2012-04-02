package org.mxhero.webapi.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "directions")
@XmlAccessorType(XmlAccessType.FIELD)
public class RuleDirections {

	private RuleDirection from;
	
	private RuleDirection to;

	public RuleDirection getFrom() {
		return from;
	}

	public void setFrom(RuleDirection from) {
		this.from = from;
	}

	public RuleDirection getTo() {
		return to;
	}

	public void setTo(RuleDirection to) {
		this.to = to;
	}
	
}
