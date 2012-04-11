package org.mxhero.console.backend.service;

import org.mxhero.console.backend.vo.QuarantineVO;

public interface QuarantineService {

	public void save(QuarantineVO quarantine);
	
	public QuarantineVO read(String domain);
	
}
