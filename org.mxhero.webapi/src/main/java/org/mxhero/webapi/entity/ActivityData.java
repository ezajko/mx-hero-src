package org.mxhero.webapi.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activityData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityData {

	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="incomming")
	private List<ResultRow> incommings;
	
	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="outgoing")
	private List<ResultRow> outgoings;
	
	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="virus")
	private List<ResultRow> virus;

	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="spam")
	private List<ResultRow> spam;
	
	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="blocked")
	private List<ResultRow> blocked;

	public List<ResultRow> getIncommings() {
		return incommings;
	}

	public void setIncommings(List<ResultRow> incommings) {
		this.incommings = incommings;
	}

	public List<ResultRow> getOutgoings() {
		return outgoings;
	}

	public void setOutgoings(List<ResultRow> outgoings) {
		this.outgoings = outgoings;
	}

	public List<ResultRow> getVirus() {
		return virus;
	}

	public void setVirus(List<ResultRow> virus) {
		this.virus = virus;
	}

	public List<ResultRow> getSpam() {
		return spam;
	}

	public void setSpam(List<ResultRow> spam) {
		this.spam = spam;
	}

	public List<ResultRow> getBlocked() {
		return blocked;
	}

	public void setBlocked(List<ResultRow> blocked) {
		this.blocked = blocked;
	}
	
}
