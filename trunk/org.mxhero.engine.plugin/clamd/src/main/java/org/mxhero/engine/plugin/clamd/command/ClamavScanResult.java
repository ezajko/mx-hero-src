package org.mxhero.engine.plugin.clamd.command;

import java.util.Collection;

import org.mxhero.engine.commons.mail.command.Result;

public class ClamavScanResult extends Result{

	private Collection<String> scanResults;

	public Collection<String> getScanResults() {
		return scanResults;
	}

	public void setScanResults(Collection<String> scanResults) {
		this.scanResults = scanResults;
	}
	
}
