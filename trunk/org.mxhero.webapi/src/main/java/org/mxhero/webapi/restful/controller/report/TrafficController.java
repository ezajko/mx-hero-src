package org.mxhero.webapi.restful.controller.report;

import org.mxhero.webapi.entity.ResultQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports/traffic")
public class TrafficController {

	@RequestMapping(value="/incomming", method = RequestMethod.GET)
	public ResultQuery incomming(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/incommingDay", method = RequestMethod.GET)
	public ResultQuery incommingDay(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/outgoing", method = RequestMethod.GET)
	public ResultQuery outgoing(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/outgoingDay", method = RequestMethod.GET)
	public ResultQuery outgoingDay(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
}
