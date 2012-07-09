package org.mxhero.engine.plugin.disclaimercontract.contractserver.service;

public interface RequestService {

	void approve(Long id, String type);

	void veto(Long id);
}
