package org.mxhero.webapi.restful.controller.report;

import org.mxhero.webapi.entity.ActivityData;
import org.mxhero.webapi.entity.ResultQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports/platform")
public class PlatformController {
	
	@RequestMapping(value="/activity", method = RequestMethod.GET)
	public ActivityData activity(String domain, String sinceDate, String sinceOffset){
		return new ActivityData();
	}

	@RequestMapping(value="/activityDay", method = RequestMethod.GET)
	public ActivityData activityDay(String domain, String sinceDate, String sinceOffset){
		return new ActivityData();
	}
	
	@RequestMapping(value="/data", method = RequestMethod.GET)
	public ResultQuery data(String domain){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/composition", method = RequestMethod.GET)
	public ResultQuery composition(String domain, String sinceDate, String sinceOffset){
		return new ResultQuery();
	}
	
}
