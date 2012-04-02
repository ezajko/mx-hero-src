package org.mxhero.webapi.restful.controller.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.mxhero.webapi.entity.ResultPage;
import org.mxhero.webapi.entity.ResultQuery;
import org.mxhero.webapi.entity.Record;
import org.mxhero.webapi.entity.ResultRow;
import org.mxhero.webapi.entity.RuleDirections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports/custom")
public class CustomController {

	@RequestMapping(value="/topTenSenders", method = RequestMethod.POST)
	public ResultQuery topTenSenders(@RequestBody RuleDirections directions, String sinceDate, String sinceOffset, String untilDate, String untilOffset){
		ResultQuery result = new ResultQuery();
		result.setRows(new ArrayList<ResultRow>());
		ResultRow row = new ResultRow();
		row.setRow(new HashMap<String,Object>());
		row.getRow().put("count", 100);
		row.getRow().put("date", Calendar.getInstance().getTime());
		result.getRows().add(row);
		return result;
	}

	@RequestMapping(value="/topTenRecipients", method = RequestMethod.POST)
	public ResultQuery topTenRecipients(@RequestBody RuleDirections directions, String sinceDate, String sinceOffset, String untilDate, String untilOffset){
		ResultQuery result = new ResultQuery();
		result.setRows(new ArrayList<ResultRow>());
		ResultRow row = new ResultRow();
		row.setRow(new HashMap<String,Object>());
		row.getRow().put("count", 100);
		row.getRow().put("date", Calendar.getInstance().getTime());
		result.getRows().add(row);
		return result;
	}
	
	@RequestMapping(value="/emails", method = RequestMethod.POST)
	public ResultPage<Record> getEmails(@RequestBody RuleDirections directions, String sinceDate, String sinceOffset, String untilDate, String untilOffset, Integer limit, Integer offset){
		ResultPage<Record> page = new ResultPage<Record>();
		page.setElements(new ArrayList<Record>());
		page.getElements().add(new Record());
		return page;
	}
}
