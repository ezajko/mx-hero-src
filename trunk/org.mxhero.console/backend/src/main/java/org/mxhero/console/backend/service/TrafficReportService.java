package org.mxhero.console.backend.service;

import java.util.Collection;

public interface TrafficReportService {

	Collection getIncomming(String domain, long since, String offset);

	Collection getIncommingByDay(String domain, long day);

	Collection getOutgoing(String domain, long since, String offset);
	
	Collection getOutgoingByDay(String domain, long day);

}
