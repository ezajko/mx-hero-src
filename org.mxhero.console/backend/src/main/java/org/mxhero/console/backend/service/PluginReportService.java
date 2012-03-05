package org.mxhero.console.backend.service;

import java.util.Collection;
import java.util.List;

public interface PluginReportService {

	Integer MAX_RESULT = 1000;
	
	Collection getResult (String queryString, List params);
	
}
