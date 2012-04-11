package org.mxhero.console.backend.repository;

import org.mxhero.console.backend.vo.QuarantineVO;

public interface QuarantineRepository {

	int save(QuarantineVO qurantine);
	
	int delete(String domain);
	
	QuarantineVO read(String domain);
}
