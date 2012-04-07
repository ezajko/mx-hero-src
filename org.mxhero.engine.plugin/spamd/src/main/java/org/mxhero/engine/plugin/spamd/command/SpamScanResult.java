package org.mxhero.engine.plugin.spamd.command;

import org.mxhero.engine.commons.mail.command.Result;

/**
 * @author mxhero
 *
 */
public class SpamScanResult extends Result{

	private Double hits;

	/**
	 * @return
	 */
	public Double getHits() {
		return hits;
	}

	/**
	 * @param hits
	 */
	public void setHits(Double hits) {
		this.hits = hits;
	}
	
}
