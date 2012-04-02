package org.mxhero.webapi.restful.controller.report;

import org.mxhero.webapi.entity.ResultPage;
import org.mxhero.webapi.entity.ResultQuery;
import org.mxhero.webapi.entity.ResultRow;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports/plugin")
public class PluginController {

	@RequestMapping(value="/query", method = RequestMethod.GET)
	public ResultQuery query(String query){
		return new ResultQuery();
	}
	
	@RequestMapping(value="/queryPage", method = RequestMethod.GET)
	public ResultPage<ResultRow> queryPage(String query, Integer limit, Integer offset){
		return new ResultPage<ResultRow>();
	}
	
}
