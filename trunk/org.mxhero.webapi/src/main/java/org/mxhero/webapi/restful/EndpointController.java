package org.mxhero.webapi.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
@RequestMapping("/help")
public class EndpointController {
	
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Autowired
	public EndpointController(RequestMappingHandlerMapping handlerMapping) {
		this.requestMappingHandlerMapping = handlerMapping;
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String show() {
		String result = "";
	    for (RequestMappingInfo element : requestMappingHandlerMapping.getHandlerMethods().keySet()) {
	        result += "<p>" + element.getPatternsCondition() + "<br>";
	        result += element.getMethodsCondition() + "<br>";
	        result += element.getParamsCondition() + "<br>";
	        result += element.getProducesCondition() + "<br>";
	    }
	    return result;
	}
}
