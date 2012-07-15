package org.mxhero.engine.plugin.disclaimercontract.contractserver.service;

public interface RequestService {

	void approve(Long id, String type, String aditionalData);

	void veto(Long id, String aditionalData);
}
