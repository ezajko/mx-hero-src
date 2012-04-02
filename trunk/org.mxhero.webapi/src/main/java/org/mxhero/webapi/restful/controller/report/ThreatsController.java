package org.mxhero.webapi.restful.controller.report;

import org.mxhero.webapi.entity.ResultPage;
import org.mxhero.webapi.entity.ResultQuery;
import org.mxhero.webapi.entity.ResultRow;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports/threats")
public class ThreatsController {

	@RequestMapping(value="/spam", method = RequestMethod.GET)
	public ResultQuery spamHits(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/spamDay", method = RequestMethod.GET)
	public ResultQuery spamHitsDay(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/virus", method = RequestMethod.GET)
	public ResultQuery virusHits(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/virusDay", method = RequestMethod.GET)
	public ResultQuery virusHitsDay(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/virusMails", method = RequestMethod.GET)
	public ResultPage<ResultRow> virusMails(String domain, String sinceDate, String sinceOffset, String untilDate, String untilOffset, Integer limit, Integer offset){
		return new ResultPage<ResultRow>();
	}
	
	@RequestMapping(value="/spamMails", method = RequestMethod.GET)
	public ResultPage<ResultRow> spamMails(String domain, String sinceDate, String sinceOffset, String untilDate, String untilOffset, Integer limit, Integer offset){
		return new ResultPage<ResultRow>();
	}
}
