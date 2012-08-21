package org.mxhero.console.backend.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService
public class AccountEndPoint {

	private EmailAccountService accountService;

	@Autowired(required=true)
	public AccountEndPoint(@Qualifier("jdbcEmailAccountService")EmailAccountService accountService) {
		this.accountService = accountService;
	}
	
	@WebMethod(action="readAccounts")
	PageVO findPageBySpecs(@WebParam(name="domainId")String domainId, 
							@WebParam(name="email")String email, 
							@WebParam(name="pageNo")int pageNo, 
							@WebParam(name="pageSize")int pageSize){
		return accountService.findPageBySpecs(domainId, email, null, pageNo, pageSize);
	}
	
	@WebMethod(action="updateProperties")
	public void updateProperties(@WebParam(name="domainId")String domainId, 
								@WebParam(name="account")String account, 
								@WebParam(name="properties")List<AccountPropertyVO> properties) {
		accountService.updateProperties(domainId, account, properties);
	}

	@WebMethod(action="updateProperties")
	public List<AccountPropertyVO> readProperties(@WebParam(name="domainId")String domainId, 
													@WebParam(name="account")String account) {
		return accountService.readProperties(domainId, account);
	}
}
