package org.mxhero.webapi.restful;

import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.PageVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/domains")
public class DomainsController {

	public PageVO<DomainVO> readAll(){
		return null;
	}
}
