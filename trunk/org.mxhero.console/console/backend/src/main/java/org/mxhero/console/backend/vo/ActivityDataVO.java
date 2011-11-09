package org.mxhero.console.backend.vo;

import java.util.Collection;

public class ActivityDataVO {

	private Collection incomming;
	private Collection outgoing;
	private Collection spam;
	private Collection blocked;
	private Collection virus;
	
	public Collection getIncomming() {
		return incomming;
	}
	public void setIncomming(Collection incomming) {
		this.incomming = incomming;
	}
	public Collection getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(Collection outgoing) {
		this.outgoing = outgoing;
	}
	public Collection getSpam() {
		return spam;
	}
	public void setSpam(Collection spam) {
		this.spam = spam;
	}
	public Collection getBlocked() {
		return blocked;
	}
	public void setBlocked(Collection blocked) {
		this.blocked = blocked;
	}
	public Collection getVirus() {
		return virus;
	}
	public void setVirus(Collection virus) {
		this.virus = virus;
	}

}
