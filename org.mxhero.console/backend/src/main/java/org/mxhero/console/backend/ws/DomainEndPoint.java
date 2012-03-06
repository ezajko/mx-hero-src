package org.mxhero.console.backend.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.service.DomainService;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService
public class DomainEndPoint {

	private DomainService domainService;
	
	@Autowired
	public DomainEndPoint(@Qualifier("jdbcDomainService")DomainService domainService) {
		this.domainService = domainService;
	}
	
	@WebMethod(action="createDomain")
	public void insert(
			@WebParam(name="domainVO")DomainVO domainVO, 
			@WebParam(name="hasAdmin")Boolean hasAdmin, 
			@WebParam(name="password")String password, 
			@WebParam(name="email")String email) {
		domainService.insert(domainVO, hasAdmin, password, email);	
	}
	
	@WebMethod(action="editDomain")
	public void edit(
			@WebParam(name="domainVO")DomainVO domainVO, 
			@WebParam(name="hasAdmin")Boolean hasAdmin, 
			@WebParam(name="password")String password, 
			@WebParam(name="email")String email) {
		domainService.edit(domainVO, hasAdmin, password, email);
	}
	
	@WebMethod(action="findAll")
	public PageVO findAll(
			@WebParam(name="domainId")String domainId, 
			@WebParam(name="pageNo")int pageNo, 
			@WebParam(name="pageSize")int pageSize) {
		return domainService.findAll(domainId, pageNo, pageSize);
	}
	
	@WebMethod(action="deleteDomain")
	public void delete(@WebParam(name="domainId")String domainId){
		domainService.remove(domainId);
	}
	
	@WebMethod(action="insertAdLdap")
	public DomainAdLdapVO insertAdLdap(@WebParam(name="adLdapVO")DomainAdLdapVO adLdapVO){
		return domainService.insertAdLdap(adLdapVO);
	}

	@WebMethod(action="editAdLdap")
	public DomainAdLdapVO editAdLdap(@WebParam(name="adLdapVO")DomainAdLdapVO adLdapVO){
		return domainService.editAdLdap(adLdapVO);
	}
	
	@WebMethod(action="deleteAdLdap")
	public void deleteAdLdap(@WebParam(name="domainId")String domainId){
		domainService.removeAdLdap(domainId);
	}

	@WebMethod(action="refreshAdLdap")
	public DomainAdLdapVO refreshAdLdap(@WebParam(name="domainId")String domainId){
		return domainService.refreshAdLdap(domainId);
	}
}
