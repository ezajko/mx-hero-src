package org.mxhero.console.backend.vo;

public class MessagesCompositionVO {

	private Long blocked;
	private Long spam;
	private Long virus;
	private Long clean;
	
	public Long getBlocked() {
		return blocked;
	}
	
	public void setBlocked(Long blocked) {
		this.blocked = blocked;
	}
	
	public Long getSpam() {
		return spam;
	}
	
	public void setSpam(Long spam) {
		this.spam = spam;
	}
	
	public Long getVirus() {
		return virus;
	}
	
	public void setVirus(Long virus) {
		this.virus = virus;
	}
	
	public Long getClean() {
		return clean;
	}
	
	public void setClean(Long clean) {
		this.clean = clean;
	}
	
}
